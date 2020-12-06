package de.qhun.declaration_provider.reducer.by_score

import de.qhun.declaration_provider.domain.Declaration
import de.qhun.declaration_provider.reducer.DeclarationReducer

object ScoreDeclarationReducer : DeclarationReducer {

    override suspend fun reduce(declarations: List<Declaration>): List<Declaration> {
        return declarations
    }
}
