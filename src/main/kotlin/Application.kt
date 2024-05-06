import app.LocalLLM
import app.VectorDatabase
import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import kotlinx.coroutines.runBlocking

suspend fun main() {
    Javalin.create { config ->
        config.bundledPlugins.enableDevLogging()
        config.staticFiles.add("/public", Location.CLASSPATH)
        config.router.mount { route ->
            route.post("/embeddings") { ctx ->
                ctx.json(runBlocking {
                    LocalLLM.embeddings(ctx.body())
                })
            }
        }
    }.start(7070)

    setupDatabase()
}

private suspend fun setupDatabase() {
    if (VectorDatabase.getCollections().result.collections.isEmpty()) {
        println(VectorDatabase.createKnowledgeCollection())
    }
}
