package de.qhun.declaration_provider.provider.gamepedia

import de.qhun.declaration_provider.domain.DataType

internal object GamepediaDataTypeInterpreter {

    private val stringType = Pair(DataType.TypeString, listOf("string", "text"))
    private val intType = Pair(DataType.TypeInt, listOf("int", "integer", "number"))
    private val floatType = Pair(DataType.TypeFloat, listOf("float"))
    private val doubleType = Pair(DataType.TypeDouble, listOf("double"))
    private val booleanType = Pair(DataType.TypeBoolean, listOf("bool", "boolean", "flag"))
    private val nullType = Pair(DataType.TypeNull, listOf("nil", "null"))
    private val undefinedType = Pair(DataType.TypeNull, listOf("undefined"))
    private val arrayType = Pair(DataType.TypeArray, listOf("[]", "array"))
    private val objectType = Pair(DataType.TypeObject, listOf("table", "object"))

    private val types = listOf(
        stringType, intType, floatType, doubleType, booleanType,
        nullType, undefinedType, arrayType, objectType
    )

    fun String.interpretDataType(): DataType {

        return types.find { pair ->
            val found = pair.second.find { candidate ->
                toLowerCase().contains(candidate)
            }
            found != null
        }?.first ?: this.let {
            if (isNotBlank() && length < 50)
                DataType.TypeRef(it.trim())
            else
                DataType.TypeUnknown
        }
    }
}
