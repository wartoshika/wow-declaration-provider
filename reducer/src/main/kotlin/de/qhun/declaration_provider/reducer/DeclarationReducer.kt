package de.qhun.declaration_provider.reducer

import de.qhun.declaration_provider.domain.Declaration

interface DeclarationReducer {

    suspend fun reduce(declarations: List<Declaration>): List<Declaration>
}
