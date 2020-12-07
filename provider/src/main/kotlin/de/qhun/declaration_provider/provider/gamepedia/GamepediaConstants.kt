package de.qhun.declaration_provider.provider.gamepedia

internal object GamepediaConstants {

    const val URL = "https://wow.gamepedia.com"
    const val URL_ENTRY_POINT = "$URL/World_of_Warcraft_API"
    const val FEATURE_ENTRY = ".mw-parser-output"

    const val DETAIL_DOCUMENTATION_ENTRY = "div#mw-content-text .mw-parser-output > p"
    const val DETAIL_ARGUMENT_ENTRY = "div#mw-content-text h2 + dl"
    const val DETAIL_RETURN_ENTRY = "div#mw-content-text dl + h2 + dl"

    const val FUNCTION_OVERVIEW_SCORE = 25
}
