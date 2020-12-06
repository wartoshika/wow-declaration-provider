package de.qhun.declaration_provider.domain.helper

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.Executors

inline fun <reified T, R> Iterable<T>.mapChunked(chunkSize: Int, noinline transformer: suspend (T) -> R): Iterable<R> =
    runBlocking {

        val parallelPool = Executors.newFixedThreadPool(chunkSize).asCoroutineDispatcher()
        val result = mutableListOf<Deferred<R>>()
        val iterableList = listOf(*this@mapChunked.toList().toTypedArray())
        iterableList.asFlow()
            .buffer(chunkSize)
            .collect {
                result.add(async(parallelPool) { transformer(it) })
            }

        result.awaitAll().also {
            parallelPool.close()
        }
    }

fun <T, B : Comparable<B>> List<T>.sortByField(field: T.() -> B) = sortedWith { a, b ->
    field(a).compareTo(field(b))
}
