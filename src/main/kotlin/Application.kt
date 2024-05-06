import com.aallam.openai.api.embedding.EmbeddingRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import kotlinx.coroutines.runBlocking

fun main() {
    Javalin.create { config ->
        config.bundledPlugins.enableDevLogging()

        config.staticFiles.add("/public", Location.CLASSPATH)

        config.router.mount { route ->
            route.post("/embeddings") { ctx ->
                val openAI = OpenAI(
                    OpenAIConfig(
                        host = OpenAIHost(baseUrl = "http://localhost:1234/v1/"),
                        token = "lm-studio"
                    )
                )

                ctx.json(runBlocking {
                    openAI.embeddings(
                        request = EmbeddingRequest(
                            model = ModelId("nomic-ai/nomic-embed-text-v1.5-GGUF"),
                            input = listOf(ctx.body())
                        )
                    )
                })
            }
        }
    }.start(7070)
}
