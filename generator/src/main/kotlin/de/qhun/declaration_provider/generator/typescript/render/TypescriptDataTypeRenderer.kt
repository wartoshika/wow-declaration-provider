package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.domain.DataType
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions

internal object TypescriptDataTypeRenderer {

    fun DataType.render(options: DeclarationGeneratorOptions): String {

        return when (this) {
            is DataType.TypeNull -> "null | undefined"

            is DataType.TypeLong,
            is DataType.TypeInt,
            is DataType.TypeFloat,
            is DataType.TypeDouble -> "number"

            is DataType.TypeString -> "string"
            is DataType.TypeBoolean -> "boolean"
            is DataType.TypeArray -> "{ [arrayIdx: number]: any }"
            is DataType.TypeObject -> "{ [objectIdx: string]: any }"
            is DataType.TypeFunction -> "(...args: any[]) => any"

            is DataType.TypeRef -> reference

            else -> "any"
        }
    }
}
