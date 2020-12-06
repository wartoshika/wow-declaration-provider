package de.qhun.declaration_provider.generator.typescript

import de.qhun.declaration_provider.domain.Declaration

sealed class TypescriptDeclarationType {

    interface DeclarationType {
        fun accept(declaration: Declaration): Boolean
        val declarations: MutableList<Declaration>
    }

    class Namespaced : TypescriptDeclarationType(), DeclarationType {
        override val declarations = mutableListOf<Declaration>()

        override fun accept(declaration: Declaration): Boolean {
            return declaration.namespace != null
        }
    }

    class Global : TypescriptDeclarationType(), DeclarationType {
        override val declarations = mutableListOf<Declaration>()

        override fun accept(declaration: Declaration): Boolean {
            return declaration.namespace == null
        }
    }
}
