package de.qhun.declaration_provider.provider.gamepedia

import de.qhun.declaration_provider.domain.*
import de.qhun.declaration_provider.domain.helper.mapChunked
import kotlinx.coroutines.coroutineScope

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
            listOf(declaration)
        }
}
