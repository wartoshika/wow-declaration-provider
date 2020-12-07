package de.qhun.declaration_provider.domain

data class SemverVersion(
    val major: Int = 1,
    val minor: Int = 0,
    val patch: Int = 0,
    val build: String? = null
) : Version {

    companion object {

        private val versionRegex = Regex("([0-9]+)\\.?([0-9]+)?\\.?([0-9]+)?\\.?-?(.*)?")

        fun String.toSemverVersion(): SemverVersion? {

            try {
                var major = 0
                var minor = 0
                var patch = 0
                var build: String? = null
                versionRegex.find(this)?.groupValues?.let {
                    major = it[1].toInt()
                    minor = it[2].toInt()
                    patch = it[3].toInt()
                    build = it[4]
                }

                if (major == 0 && minor == 0 && patch == 0 && build == null) {
                    return null
                }

                return SemverVersion(
                    major, minor, patch, build
                )
            } catch (e: Throwable) {
                return null
            }
        }
    }

    override fun full() = listOf(
        major, minor, patch
    ).joinToString(".") + build?.let {
        "-$it"
    }
}
