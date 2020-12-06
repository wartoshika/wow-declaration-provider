package de.qhun.declaration_provider.provider.github

import de.qhun.declaration_provider.domain.WowVersion

internal object GithubConstants {

    const val GIT_API_URL = "https://api.github.com/repos/Gethe/wow-ui-source/contents/AddOns/Blizzard_APIDocumentation"
    const val FUNCTION_SCORE = 100
    const val PARAMETER_SCORE = 10
    const val RETURN_SCORE = 10
    const val ENUM_SCORE = 50
    const val EVENT_SCORE = 50
    val BRANCH_NAME_BY_VERSION = mapOf(
        Pair(WowVersion.RETAIL, "live"),
        Pair(WowVersion.BETA, "beta"),
        Pair(WowVersion.CLASSIC, "classic"),
        Pair(WowVersion.PTR, "ptr"),
    )
    val ignorableFiles = listOf(
        "BaseAPIMixin.lua",
        "FieldsAPIMixin.lua",
        "FunctionsAPIMixin.lua",
        "SystemsAPIMixin.lua",
        "TablesAPIMixin.lua",
        "EventsAPIMixin.lua",
        "Blizzard_APIDocumentation.lua",
        "Blizzard_APIDocumentation.toc"
    )
    val luaFix = """
        Enum = {
            PlayerCurrencyFlagsDbFlags = {
                InBackpack = 0,
                UnusedInUI = 0
            }
        }
    """.trimIndent()
}
