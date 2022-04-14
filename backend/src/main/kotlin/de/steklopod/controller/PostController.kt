package de.steklopod.controller

import de.steklopod.model.Comment
import de.steklopod.model.Post
import de.steklopod.repository.CommentRepository
import de.steklopod.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/posts")
class PostController(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) {

    @GetMapping
    fun findAll(): Flow<Post> = postRepository.findAll()

    @GetMapping("count")
    suspend fun count(): Long = postRepository.count()

    @GetMapping("{id}")
    suspend fun findOne(@PathVariable id: String): Post = postRepository.findOne(id)

    @PostMapping
    suspend fun save(@RequestBody post: Post): Post = postRepository.save(post)

    @GetMapping("{id}/comments")
    fun findCommentsByPostId(@PathVariable id: String): Flow<Comment> = commentRepository.findByPostId(id)

    @GetMapping("{id}/comments/count")
    suspend fun countCommentsByPostId(@PathVariable id: String): Long = commentRepository.countByPostId(id)

    @PostMapping("{id}/comments")
    suspend fun saveComment(@PathVariable id: String, @RequestBody comment: Comment): Comment =
        commentRepository.save(comment.copy(postId = id, content = comment.content))
}
