package de.qhun.declaration_provider.provider.gamepedia

import de.qhun.declaration_provider.domain.Declaration
import de.qhun.declaration_provider.domain.WowVersion
import de.qhun.declaration_provider.provider.DeclarationProvider
import de.qhun.declaration_provider.provider.gamepedia.GamepediaDetailDownloader.downloadDetails
import kotlinx.coroutines.coroutineScope

object GamepediaDeclarationProvider : DeclarationProvider {

    override suspend fun provide(version: WowVersion): List<Declaration> = coroutineScope {

        GamepediaOverviewDownloader
            .downloadOverview(version)
            .downloadDetails()
    }
}
