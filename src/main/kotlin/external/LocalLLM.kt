package external

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI
import io.github.amithkoujalgi.ollama4j.core.models.OllamaResult
import io.github.amithkoujalgi.ollama4j.core.utils.Options

object LocalLLM {

    private val ollama = OllamaAPI("http://localhost:11434")

    fun message(text: String): OllamaResult =
        ollama.generate(
            "phi3",
            text,
            Options(emptyMap())
        )

    fun embeddings(text: String): List<Double> =
        ollama.generateEmbeddings("nomic-embed-text", text)

}
