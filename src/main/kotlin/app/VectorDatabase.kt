package app

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson


private const val COLLECTION_NAME = "knowledge"

object VectorDatabase {

    private val client = HttpClient(Java) {
        install(ContentNegotiation) {
            jackson()
        }
    }

    suspend fun getCollections(): CollectionsResponse {
        return client.get("http://localhost:6333/collections").body()
    }

    suspend fun createKnowledgeCollection(): String {
        return client.put("http://localhost:6333/collections/$COLLECTION_NAME") {
            contentType(ContentType.Application.Json)
            setBody(CreateCollectionRequest(
                VectorParameters(
                    size = 2048,
                    distance = "Cosine"
                )
            ))
        }.bodyAsText()
    }


}

// Retrieve collections
data class CollectionsResponse(val result: CollectionsResult, val status: String, val time: Double)
data class CollectionsResult(val collections: List<CollectionDetails>)
data class CollectionDetails(val name: String)

// Create a collection
data class CreateCollectionRequest(val vectors: VectorParameters)
data class VectorParameters(val size: Int, val distance: String)

