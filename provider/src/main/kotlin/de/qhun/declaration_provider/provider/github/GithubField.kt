package de.qhun.declaration_provider.provider.github

import de.qhun.declaration_provider.domain.DataType
import de.qhun.declaration_provider.domain.Documentation

internal data class GithubField(
    val name: String,
    val type: String,
    val nillable: Boolean = true,
    val innerType: String? = null,
    val enumValue: Any? = null,
    val default: Boolean? = false,
    val documentation: Documentation? = null
) {

    fun getDataType(): DataType {
        return when (type) {
            "bool" -> DataType.TypeBoolean
            "string" -> DataType.TypeString
            "number" -> DataType.TypeInt
            "nil" -> DataType.TypeNull
            "table" -> if (innerType == null) {
                DataType.TypeObject
            } else {
                DataType.TypeRef(innerType)
            }
            else -> if (type != name)
                DataType.TypeRef(type)
            else DataType.TypeUnknown
        }
    }
}
