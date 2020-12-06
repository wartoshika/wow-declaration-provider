package de.qhun.declaration_provider.domain

sealed class DataType {

    // primitive types

    object TypeNull : DataType()
    object TypeString : DataType()
    object TypeBoolean : DataType()
    object TypeLong : DataType()
    object TypeInt : DataType()
    object TypeDouble : DataType()
    object TypeFloat : DataType()
    object TypeObject : DataType()
    object TypeArray : DataType()
    object TypeFunction : DataType()
    object TypeAny : DataType()

    // complex types
    class TypeRef(val reference: String) : DataType()

    // documentation types
    object TypeUnknown : DataType()

}

