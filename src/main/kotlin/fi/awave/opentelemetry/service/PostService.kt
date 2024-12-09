package fi.awave.opentelemetry.service

import fi.awave.opentelemetry.model.Post
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class PostService(
    restClientBuilder: RestClient.Builder
) {

    private val restClient = restClientBuilder
        .baseUrl("https://jsonplaceholder.typicode.com")
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