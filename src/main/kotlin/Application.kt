import app.ConversationHandler
import external.LocalLLM
import external.Point
import external.VectorDatabase
import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import kotlinx.coroutines.runBlocking

suspend fun main() {
    Javalin.create { config ->
        config.bundledPlugins.enableDevLogging()
        config.staticFiles.add("/public", Location.CLASSPATH)
        config.router.mount { route ->
            route.post("/embeddings") { ctx ->
                ctx.result(runBlocking {
                    VectorDatabase.search(LocalLLM.embeddings(ctx.body())).toString()
                })
            }
            route.post("/message") { ctx ->
                ctx.json(runBlocking {
                    ConversationHandler.handle(ctx.body())
                })
            }
        }
    }.start(7070)

    setupDatabase()
}

private suspend fun setupDatabase() {
    if (VectorDatabase.getCollections().result.collections.isEmpty()) {
        println(VectorDatabase.createKnowledgeCollection())
        insertSampleEmbeddings()
    }
}


private suspend fun insertSampleEmbeddings() {
    println(
        VectorDatabase.insert(
            listOf(
                Point(
                    1,
                    LocalLLM.embeddings("Turn on the lights"),
                    mapOf(
                        "request" to "Turn on the lights",
                        "action" to "turn_on",
                        "device" to "all_lights",
                        "domain" to "lights"
                    )
                ),
                Point(
                    2,
                    LocalLLM.embeddings("Turn off the lights"),
                    mapOf(
                        "request" to "Turn off the lights",
                        "action" to "turn_off",
                        "device" to "all_lights",
                        "domain" to "lights"
                    )
                ),
                Point(
                    3,
                    LocalLLM.embeddings("Turn on the computer"),
                    mapOf(
                        "request" to "Turn on the computer",
                        "action" to "turn_on",
                        "device" to "computer_plug",
                        "domain" to "plug"
                    )
                ),
                Point(
                    4,
                    LocalLLM.embeddings("Turn off the computer"),
                    mapOf(
                        "request" to "Turn off the computer",
                        "action" to "turn_off",
                        "device" to "computer_plug",
                        "domain" to "plug"
                    )
                )
            )
        )
    )
}
