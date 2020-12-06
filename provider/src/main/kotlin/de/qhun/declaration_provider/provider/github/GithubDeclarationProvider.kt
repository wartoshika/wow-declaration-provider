package de.qhun.declaration_provider.provider.github

import de.qhun.declaration_provider.domain.Declaration
import de.qhun.declaration_provider.domain.WowVersion
import de.qhun.declaration_provider.provider.DeclarationProvider
import kotlinx.coroutines.coroutineScope

object GithubDeclarationProvider : DeclarationProvider {

    override suspend fun provide(version: WowVersion): List<Declaration> = coroutineScope {
        GithubLuaParser.parseFiles(
            GithubFileDownloader.downloadDocumentation(version)
        )
    }
}

