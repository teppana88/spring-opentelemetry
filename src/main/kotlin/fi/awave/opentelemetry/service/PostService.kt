package fi.awave.opentelemetry.service

import fi.awave.opentelemetry.model.Post
import io.micrometer.tracing.Tracer
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class PostService(
    restClientBuilder: RestClient.Builder,
    private val tracer: Tracer
) {

    private val restClient = restClientBuilder
        .baseUrl("https://jsonplaceholder.typicode.com")
        // Grafana Tempo Service Graph requires peer.service tag to be set
        // When present in the span, the peer.service tag is used to show span as a separate service in the Service Graph
        .requestInterceptor { request, body, execution ->
            val currentSpan = tracer.currentSpanCustomizer()
            currentSpan?.tag("peer.service", "ext-service")
            currentSpan?.name("ext-span-name")
            execution.execute(request, body)
        }
        .build()

    fun getAllPosts(): List<Post> {
        return restClient
            .get()
            .uri("/posts")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body(object : ParameterizedTypeReference<List<Post>>() {})
            ?: emptyList()
    }

    fun getPostById(id: Int): Post {
        return restClient
            .get()
            .uri("/posts/{id}", id)
            .accept(APPLICATION_JSON)
            .retrieve()
            .body(Post::class.java)
            ?: throw IllegalArgumentException("Post not found")
    }
}