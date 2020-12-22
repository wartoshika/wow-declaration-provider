package de.qhun.declaration_provider.provider.gamepedia

import de.qhun.declaration_provider.domain.*
import de.qhun.declaration_provider.domain.helper.mapChunked
import de.qhun.declaration_provider.provider.gamepedia.GamepediaDataTypeInterpreter.interpretDataType
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

internal object GamepediaDetailDownloader {

    suspend fun List<Declaration>.downloadDetails(): List<Declaration> = coroutineScope {

        mapChunked(10) {
            when (it) {
                is FunctionDeclaration -> {

                    if (it.documentation?.url != null) {
                        downloadFunctionDetails(it)
                    } else {
                        listOf(convertToUnknownDocumentationItem(it))
                    }

                }
                else -> listOf(it)
            }
        }.flatten()
    }

    private fun convertToUnknownDocumentationItem(declaration: FunctionDeclaration): FunctionDeclaration {
        return FunctionDeclaration(
            name = declaration.name,
            namespace = declaration.namespace,
            returns = listOf(
                ReturnFragment(
                    name = "unknownReturn",
                    type = DataType.TypeUnknown
                )
            ),
            parameter = listOf(
                ParameterFragment(
                    name = "unknownArguments",
                    type = DataType.TypeUnknown
                )
            )
        )
    }

    private suspend fun downloadFunctionDetails(declaration: FunctionDeclaration): List<FunctionDeclaration> =
        coroutineScope {

            val document = Jsoup.connect(declaration.documentation!!.url!!).get()

            val documentation = document.select(GamepediaConstants.DETAIL_DOCUMENTATION_ENTRY)
            val parameter = document.select(GamepediaConstants.DETAIL_ARGUMENT_ENTRY)
            val parameterVariant2 = parameter.select("dd > dl")
            val returns = document.select(GamepediaConstants.DETAIL_RETURN_ENTRY)
            val returnsVariant2 = returns.select("dd > dl")

            listOf(declaration.copy(
                documentation = declaration.documentation?.copy(
                    text = documentation.text()?.let {
                        if (it.isNotBlank()) it else null
                    } ?: declaration.documentation?.text
                ),
                parameter = extractLists(if (parameterVariant2.size > 0) parameterVariant2 else parameter),
                returns = extractLists(if (returnsVariant2.size > 0) returnsVariant2 else returns)
            ))
        }

    private inline fun <reified T : Fragment> extractLists(elements: Elements): List<T> {

        val iterablePairs = mutableListOf<Pair<Element, Element>>()

        var currentTemp: Element? = null
        val accessor = elements.first()?.children() ?: elements
        accessor.forEach {
            currentTemp = if (currentTemp == null) {
                it
            } else {
                iterablePairs.add(Pair(currentTemp!!, it))
                null
            }
        }

        return iterablePairs.map {
            val name = it.first.text()
            val typeAndDocumentation = it.second.text()

            val dataType: DataType
            val documentation: String?
            if (typeAndDocumentation.contains("-")) {
                val fragments = typeAndDocumentation.split("-")
                dataType = fragments[0].interpretDataType()
                documentation = fragments[1]
            } else {
                documentation = null
                dataType = typeAndDocumentation.interpretDataType()
            }

            when (T::class) {
                ParameterFragment::class -> ParameterFragment(
                    name = name,
                    type = dataType,
                    documentation = documentation?.let { doc ->
                        Documentation(text = doc)
                    }
                ) as T
                ReturnFragment::class -> ReturnFragment(
                    name = name,
                    type = dataType,
                    documentation = documentation?.let { doc ->
                        Documentation(text = doc)
                    }
                ) as T
                else -> throw IllegalStateException("Extracting elements from a list to a class of type ${T::class.simpleName} is currently not supported!")
            }
        }
    }
}
