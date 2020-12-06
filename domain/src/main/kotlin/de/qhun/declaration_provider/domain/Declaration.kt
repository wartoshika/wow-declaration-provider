package de.qhun.declaration_provider.domain

/**
 * heart of the workflow. transports functions, constants, classes ... over
 * module boundaries and provides a simple structure and a marker interface for different implementations.
 */
interface Declaration : Fragment {
    override val name: String
    override val score: Int
    override val documentation: Documentation?

    /**
     * declaration should be considered deprecated
     */
    val deprecated: Boolean?

    /**
     * indicates that this object is accessible unter the given namespace
     */
    val namespace: String?
}
