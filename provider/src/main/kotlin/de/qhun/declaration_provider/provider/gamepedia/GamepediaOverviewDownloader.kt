package de.qhun.declaration_provider.provider.gamepedia

import de.qhun.declaration_provider.domain.*
import de.qhun.declaration_provider.domain.SemverVersion.Companion.toSemverVersion
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag

internal object GamepediaOverviewDownloader {

    suspend fun downloadOverview(version: WowVersion): List<Declaration> = coroutineScope {

        val document = Jsoup.connect(GamepediaConstants.URL_ENTRY_POINT).get()

        document
            .select(GamepediaConstants.FEATURE_ENTRY)
            // every feature is stored after a h3 element
            .select("h3")
            .map { elm ->

                val name: String
                val description: String?
                elm.also {
                    // this h3 contains the name of the feature
                    name = it.text()
                }
                    // next sibling is the description for the feature
                    .nextElementSibling()
                    .also {
                        description = it.text()
                    }
                    // now a DL is present with all functions for this feature
                    .nextElementSibling()
                    .select("dd").map {
                        extractFunction(name, description, it)
                    }
            }.flatten()
    }

    private fun extractFunction(featureName: String, featureDescription: String?, elm: Element): FunctionDeclaration {

        // this element <DD> contains a <A> element with the detail url and the function name
        // also a text block with arguments and a description is present
        // if a text node is present before the <A> element it represents the flags
        val firstChild = elm.children()[0]
        val flags = if (firstChild.tag() != Tag.valueOf("a")) {
            // flags found!
            firstChild.text()
        } else null

        val link = elm.select("a").first()
        val nodeAfterLink = link.nextSibling()

        // the description is places after a dash char
        // only select this information
        val description = nodeAfterLink?.attr("#text")?.let {
            if (it.contains("-") && it.indexOf("-") > -1) {
                val start = it.indexOf("-")
                it.substring(start + 1).trim()
            } else null
        }

        val names = nameAndNamespace(link.text());

        return FunctionDeclaration(
            name = names.second,
            namespace = names.first,
            documentation = Documentation(
                text = description,
                url = link.attr("href").let {
                    if (it.contains("redlink=1")) {
                        null
                    } else {
                        if (it.startsWith("http"))
                            it
                        else
                            GamepediaConstants.URL + it
                    }
                },
                since = (featureName + featureDescription + description).toSemverVersion()
            ),
            deprecated = flags?.toLowerCase()?.contains("deprecated"),
            score = GamepediaConstants.FUNCTION_OVERVIEW_SCORE,
            parameter = listOf(),
            returns = listOf()
        )
    }

    private fun nameAndNamespace(name: String): Pair<String?, String> {

        return name.split(".").let {
            if (it.size == 1) {
                Pair(null, it[0])
            } else {
                Pair(it[0], it[1])
            }
        }
    }
}
