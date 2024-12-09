package fi.awave.opentelemetry.controller

import fi.awave.opentelemetry.model.Post
import fi.awave.opentelemetry.service.PostService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("")
class PostController(
    private val postService: PostService
) {

    @GetMapping("/post", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPosts(): List<Post> {
        return postService.getAllPosts()
    }

    @GetMapping("/post/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPostById(@PathVariable id: Int): Post {
        return postService.getPostById(id)
    }

}