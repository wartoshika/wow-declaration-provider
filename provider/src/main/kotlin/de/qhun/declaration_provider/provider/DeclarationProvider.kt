package de.qhun.declaration_provider.provider

import de.qhun.declaration_provider.domain.Declaration
import de.qhun.declaration_provider.domain.WowVersion

interface DeclarationProvider {

    suspend fun provide(version: WowVersion): List<Declaration>
}
