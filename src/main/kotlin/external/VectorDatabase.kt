package external

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson


private const val COLLECTION_NAME = "knowledge"

object VectorDatabase {

    private var id = 1
        get() = field++

    private val client = HttpClient(Java) {
        install(ContentNegotiation) {
            jackson()
        }
    }

    suspend fun getCollections(): CollectionsResponse =
        client.get("http://localhost:6333/collections").body()

    suspend fun createKnowledgeCollection(): String =
        client.put("http://localhost:6333/collections/$COLLECTION_NAME") {
            contentType(ContentType.Application.Json)
            setBody(
                CreateCollectionRequest(
                    VectorParameters(
                        size = 768,
                        distance = "Cosine"
                    )
                )
            )
        }.bodyAsText()

    suspend fun search(embeddings: List<Double>): SearchResponse =
        client.post("http://localhost:6333/collections/$COLLECTION_NAME/points/search") {
            contentType(ContentType.Application.Json)
            setBody(
                SearchRequest(
                    vector = embeddings,
                    limit = 1,
                    with_payload = true
                )
            )
        }.body()

    suspend fun insert(vectors: List<Point>): String =
        client.put("http://localhost:6333/collections/$COLLECTION_NAME/points") {
            contentType(ContentType.Application.Json)
            setBody(InsertRequest(vectors))
        }.bodyAsText()

}


// Retrieve collections
data class CollectionsResponse(val result: CollectionsResult, val status: String, val time: Double)
data class CollectionsResult(val collections: List<CollectionDetails>)
data class CollectionDetails(val name: String)

// Create a collection
data class CreateCollectionRequest(val vectors: VectorParameters)
data class VectorParameters(val size: Int, val distance: String)

// Search
data class SearchRequest(val vector: List<Double>, val limit: Int, val with_payload: Boolean)
data class SearchResponse(val result: List<SearchResult>, val status: String, val time: Double)
data class SearchResult(
    val id: Int,
    val version: Int,
    val score: Double,
    val payload: Map<String, String>?,
    val vector: List<Double>?
)

// Insert
data class InsertRequest(val points: List<Point>)
data class Point(val id: Int, val vector: List<Double>, val payload: Map<String, String>)
