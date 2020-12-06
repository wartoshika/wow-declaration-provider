package de.qhun.declaration_provider.domain

data class Documentation(

    /**
     * human readable additional documentation text.
     */
    val text: String? = null,

    /**
     * an url including protocol to the documentation source on the internet.
     * should be accessible by anyone.
     */
    val url: String? = null,

    /**
     * since when is this declaration included.
     */
    val since: Version? = null
)
