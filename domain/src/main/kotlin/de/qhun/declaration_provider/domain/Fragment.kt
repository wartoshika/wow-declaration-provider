package de.qhun.declaration_provider.domain

interface Fragment {

    /**
     * the name of the declaration. example may be a function or parameter name.
     */
    val name: String?

    /**
     * a numeric score set by the provider to indicate the quality of the documentation source.
     * a higher value means better quality. this is important for the reducer.
     */
    val score: Int

    /**
     * additional documentation for this fragment
     */
    val documentation: Documentation?
}
