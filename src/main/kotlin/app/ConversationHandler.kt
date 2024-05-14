package app

import external.LocalLLM
import external.VectorDatabase

object ConversationHandler {

    suspend fun handle(text: String): Any {
        val embeddings = LocalLLM.embeddings(text)
        val searchResult = VectorDatabase.search(embeddings).result
        return if (searchResult.isNotEmpty() && searchResult.first().score > 0.75) {
            searchResult
        } else {
            LocalLLM.message(text)
        }
    }

}

