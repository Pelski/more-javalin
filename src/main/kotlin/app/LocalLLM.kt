package app

import com.aallam.openai.api.embedding.EmbeddingRequest
import com.aallam.openai.api.embedding.EmbeddingResponse
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost

object LocalLLM {

    private val openAI = OpenAI(
        OpenAIConfig(
            host = OpenAIHost(baseUrl = "http://localhost:1234/v1/"),
            token = "lm-studio"
        )
    )

    suspend fun embeddings(text: String): EmbeddingResponse {
        return openAI.embeddings(
            request = EmbeddingRequest(
                model = ModelId("nomic-ai/nomic-embed-text-v1.5-GGUF"),
                input = listOf(text)
            )
        )
    }
}
