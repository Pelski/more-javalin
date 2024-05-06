import io.javalin.Javalin
import io.javalin.http.sse.SseClient
import io.javalin.http.staticfiles.Location
import java.util.concurrent.Executors

fun main() {
    val sseClients = mutableListOf<SseClient>()
    generateRandomValuesAndSendToSseClientsAndRenameLater(sseClients)

    val app = Javalin.create { config ->
        config.staticFiles.add("/public", Location.CLASSPATH)
    }.start(7070)

    app.sse("/sse-stream") { sse ->
        sse.keepAlive()
        sseClients.add(sse)
        sse.sendEvent("welcome", "Hello World")

        sse.onClose {
            sseClients.remove(sse)
        }
    }
}

private fun generateRandomValuesAndSendToSseClientsAndRenameLater(sseClients: MutableList<SseClient>) {
    val executor = Executors.newVirtualThreadPerTaskExecutor();
    executor.submit {
        while (true) {
            val randomValue = (0..100).random()
            sseClients.forEach { it.sendEvent("add", randomValue) }
            Thread.sleep(2500)
        }
    }
}


