package de.qhun.declaration_provider.provider.github

import de.qhun.declaration_provider.domain.*
import org.luaj.vm2.LuaNil
import org.luaj.vm2.LuaValue

internal object GithubDataExtractor {

    fun extractData(value: LuaValue?, name: String, type: String, namespace: String?): List<Declaration> {

        if (value == null || value is LuaNil) {
            return listOf()
        }

        return (1..value.length()).flatMap {
            parseCandidate(value.get(it), namespace)
        }.filterNotNull()
    }

    private fun parseCandidate(candidate: LuaValue, namespace: String?): List<Declaration?> {

        val type = candidate.get("Type").toString()
        @Suppress("IMPLICIT_CAST_TO_ANY")
        return when (type) {
            "Event" -> listOf(parseEvent(candidate, namespace))
            "Function" -> listOf(parseFunction(candidate, namespace))
            "Structure" -> listOf(parseStructure(candidate, namespace))
            "Enumeration" -> listOf(parseEnumeration(candidate, namespace))
            // "Constants" -> parseConstant(candidate)
            else -> {
                println("Ignoring type $type because it is unsupported!")
                listOf(null)
            }
        }
    }

    private fun parseFunction(value: LuaValue, namespace: String?): FunctionDeclaration? {

        val name = value.getNullable("Name") { it.toString() }
        if (name == null) {
            println("o Function without a name is unsupported.")
            return null
        }
        val arguments = value.get("Arguments")
        val returns = value.get("Returns")
        val documentation = value.getNullable("Documentation") {
            it.get(1)?.toString()
        }

        return FunctionDeclaration(
            name = name,
            score = GithubConstants.FUNCTION_SCORE,
            namespace = namespace,
            parameter = parseFields(arguments).map {
                ParameterFragment(
                    name = it.name,
                    score = GithubConstants.PARAMETER_SCORE,
                    canBeNull = it.nillable,
                    type = it.getDataType(),
                    documentation = it.documentation
                )
            },
            returns = parseFields(returns).map {
                ReturnFragment(
                    name = it.name,
                    score = GithubConstants.RETURN_SCORE,
                    type = it.getDataType(),
                    documentation = it.documentation
                )
            },
            deprecated = documentation?.contains("deprecated") ?: false,
            documentation = Documentation(
                text = documentation
            )
        )
    }

    private fun parseEnumeration(value: LuaValue, namespace: String?): EnumDeclaration? {

        val name = value.getNullable("Name") { it.toString() }
        if (name == null) {
            println("o Enumeration without a name is unsupported.")
            return null
        }
        val documentation = value.getNullable("Documentation") {
            it.get(1)?.toString()
        }

        return EnumDeclaration(
            name = name,
            score = GithubConstants.ENUM_SCORE,
            namespace = namespace,
            deprecated = documentation?.contains("deprecated") ?: false,
            documentation = Documentation(
                text = documentation
            ),
            entries = parseFields(value.getNullable("Fields")).map {
                ConstantDeclaration(
                    name = it.name,
                    score = GithubConstants.ENUM_SCORE,
                    documentation = it.documentation,
                    deprecated = false,
                    type = it.getDataType().let { type ->
                        if (type == DataType.TypeUnknown && it.enumValue != null) DataType.TypeInt else type
                    },
                    value = "" + (it.enumValue ?: "")
                )
            }
        )
    }

    private fun parseStructure(value: LuaValue, namespace: String?): InterfaceDeclaration? {

        val name = value.getNullable("Name") { it.toString() }
        if (name == null) {
            println("o Structure without a name is unsupported.")
            return null
        }
        val documentation = value.getNullable("Documentation") {
            it.get(1)?.toString()
        }

        return InterfaceDeclaration(
            name = name,
            score = GithubConstants.ENUM_SCORE,
            namespace = namespace,
            deprecated = documentation?.contains("deprecated") ?: false,
            documentation = Documentation(
                text = documentation
            ),
            fields = parseFields(value.getNullable("Fields")).map {
                PropertyDeclaration(
                    name = it.name,
                    score = GithubConstants.ENUM_SCORE,
                    documentation = it.documentation,
                    deprecated = false,
                    type = it.getDataType(),
                    namespace = null
                )
            }
        )
    }

    private fun parseEvent(value: LuaValue, namespace: String?): EventDeclaration? {

        val name = value.getNullable("LiteralName") { it.toString() }
        if (name == null) {
            println("o Event without a literal name is unsupported.")
            return null
        }
        val documentation = value.getNullable("Documentation") {
            it.get(1)?.toString()
        }

        return EventDeclaration(
            name = name,
            score = GithubConstants.EVENT_SCORE,
            namespace = namespace,
            deprecated = documentation?.contains("deprecated") ?: false,
            documentation = Documentation(
                text = documentation
            ),
            payload = parseFields(value.getNullable("Fields")).map {
                ParameterFragment(
                    name = it.name,
                    score = GithubConstants.PARAMETER_SCORE,
                    documentation = it.documentation,
                    type = it.getDataType()
                )
            }
        )
    }

    private fun parseFields(fieldsTable: LuaValue?): List<GithubField> {

        if (fieldsTable == null || fieldsTable is LuaNil) {
            return listOf()
        }

        return (1..fieldsTable.length()).map { index ->
            val field = fieldsTable.get(index)

            val name = field.getNullable("Name") { it.toString() }
            val type = field.getNullable("Type") { it.toString() }
            val enumVal = field.getNullable("EnumValue") { it.toint() }
            val nillable = field.getNullable("Nilable") { it.toboolean() }
            val innerType = field.getNullable("InnerType") { it.toString() }
            val default = field.getNullable("Default") { it.toboolean() }
            val documentation = field.getNullable("Documentation") {
                it.get(1)?.toString()
            }

            GithubField(
                name = name!!,
                type = type!!,
                enumValue = enumVal,
                nillable = nillable ?: false,
                innerType = innerType,
                default = default,
                documentation = documentation?.let { Documentation(text = documentation) }
            )
        }
    }
}
