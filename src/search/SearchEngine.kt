package search

import java.io.File

interface SearchEngine {

    enum class STRATEGY {
        ALL, ANY, NONE
    }

    fun addEntity(rawEntity: String): Boolean

    fun find(query: String, strategy: STRATEGY): List<String>

    fun getAllEntity(): List<String>
}

class SearchEngineImpl(var storagePath: String) : SearchEngine, StorageLoader {


    private val storageEntityIndex = mutableMapOf<String, MutableSet<Int>>()
    private val storageRawEntity = mutableListOf<String>()

    init {
        loadStorage()
    }

    override fun addEntity(rawEntity: String): Boolean {
        storageRawEntity.add(rawEntity)
        val indexEntity = storageRawEntity.size - 1
        rawEntity.uppercase().split(" ").forEach {
            val word = it.trim()
            var indexes = storageEntityIndex[word]
            if (indexes == null) {
                indexes = mutableSetOf(indexEntity)
                storageEntityIndex += Pair(it, indexes)
            }
            indexes += indexEntity
        }
        return true
    }


    override fun find(query: String, strategy: SearchEngine.STRATEGY): List<String> {
        val words = query.uppercase().split(" ")
        return when (strategy) {
            SearchEngine.STRATEGY.ANY -> {
                findAny(words)
            }
            SearchEngine.STRATEGY.ALL -> {
                if (words.size == 1) {
                    findAny(words)
                }
                val indexes = mutableSetOf<Int>()
                words.forEach { word ->
                    storageEntityIndex[word]?.let {
                        if (indexes.isEmpty()) indexes.addAll(it)
                        else indexes.retainAll(it)
                    }
                }
                storageRawEntity.filterIndexed { index, _ -> index in indexes }
            }
            SearchEngine.STRATEGY.NONE -> {
                val indexes = findAnyIndexes(words)
                storageRawEntity.filterIndexed { index, _ -> index !in indexes }
            }
        }
    }

    private fun findAny(words: List<String>): List<String> {
        val indexes = findAnyIndexes(words)
        return storageRawEntity.filterIndexed { index, _ -> index in indexes }
    }

    private fun findAnyIndexes(words: List<String>): Set<Int> {
        val indexes = mutableSetOf<Int>()
        words.forEach { word ->
            storageEntityIndex[word]?.let { indexes.addAll(it) }
        }
        return indexes
    }

    override fun getAllEntity(): List<String> {
        return storageRawEntity.toList()
    }

    override fun loadStorage() {
        File(storagePath).readLines().forEach {
            addEntity(it)
        }
    }
}


interface StorageLoader {
    fun loadStorage()
}