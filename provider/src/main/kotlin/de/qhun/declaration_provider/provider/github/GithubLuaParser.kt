package de.qhun.declaration_provider.provider.github

import de.qhun.declaration_provider.domain.Declaration
import de.qhun.declaration_provider.domain.helper.mapChunked
import kotlinx.coroutines.coroutineScope
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform

internal object GithubLuaParser {

    suspend fun parseFiles(files: Map<String, String>): List<Declaration> = coroutineScope {

        val globals: Globals = JsePlatform.standardGlobals()
        files.entries
            .filter { !GithubConstants.ignorableFiles.contains(it.key) }
            .mapChunked(10) {

                val chunk = globals.load(applyFileLoaderFix(it.value), it.key)
                val docTable = chunk.call()
                extractDeclarations(docTable)
            }
            .flatten()
            .toList()
    }

    private fun applyFileLoaderFix(file: String): String {

        val localIndicator = "local"
        val endIndicator = "APIDocumentation:AddDocumentationTable"
        val startIndex = file.indexOf(localIndicator) + localIndicator.length
        val tableName = file.substring(startIndex + 1, file.indexOf(" ", startIndex + 1))

        return GithubConstants.luaFix + "\n" + file.substring(
            0,
            file.indexOf(endIndicator).let { if (it == -1) file.length - 1 else it }
        ) + "\nreturn $tableName"
    }

    private fun extractDeclarations(doc: LuaValue): List<Declaration> {

        val name = doc.get("Name").toString()
        val type = doc.get("Type").toString()
        val namespace = doc.getNullable("Namespace") { it.toString() }

        val functions = doc.get("Functions")
        val events = doc.get("Events")
        val tables = doc.get("Tables")

        return listOf(
            functions, events, tables
        ).flatMap { GithubDataExtractor.extractData(it, name, type, namespace) }
    }
}
