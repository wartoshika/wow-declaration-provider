package de.qhun.declaration_provider.provider.github

import de.qhun.declaration_provider.domain.WowVersion
import de.qhun.declaration_provider.domain.helper.mapChunked
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

@Suppress("BlockingMethodInNonBlockingContext")
internal object GithubFileDownloader {

    private val httpClient = OkHttpClient()

    suspend fun downloadDocumentation(version: WowVersion): Map<String, String> = coroutineScope {

        provideFileList(version)
            .entries
            .mapChunked(25) { entry ->
                val fileRequest = Request.Builder()
                    .url(entry.value)
                    .build()
                val response = httpClient.newCall(fileRequest).execute()
                if (response.isSuccessful && response.body != null) {
                    Pair(entry.key, response.body!!.string())
                } else {
                    throw IllegalStateException("" + response.code + ": " + response.message)
                }
            }.toMap()
    }

    private fun provideFileList(version: WowVersion): Map<String, String> {

        val request = Request.Builder()
            .url(
                listOf(
                    GithubConstants.GIT_API_URL,
                    "?ref=",
                    GithubConstants.BRANCH_NAME_BY_VERSION[version]
                ).joinToString("")
            )
            .build()
        val response = httpClient.newCall(request).execute()

        return if (!response.isSuccessful || response.body == null) {
            throw IllegalStateException("" + response.code + ": " + response.message)
        } else {

            val jsonString = response.body!!.string()
            val json = JSONArray(jsonString)
            val fileList = mutableListOf<JSONObject>()
            for (index in 0 until json.length()) {
                fileList.add(json.getJSONObject(index))
            }

            fileList.map {
                it.getString("name") to it.getString("download_url")
            }.toMap()
        }
    }
}
