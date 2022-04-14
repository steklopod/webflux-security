package de.steklopod.controller


import de.steklopod.config.security.JWTService
import de.steklopod.model.Post
import de.steklopod.model.Role
import de.steklopod.repository.CommentRepository
import de.steklopod.repository.PostRepository
import kotlinx.coroutines.reactive.asFlow
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito.times
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux

@Disabled
@WebFluxTest(controllers = [PostController::class])
class PostControllerTests(
    @Autowired private val client: WebTestClient,
    @Autowired private val jwtService: JWTService
) {


    @MockBean
    private lateinit var posts: PostRepository

    @MockBean
    private lateinit var comments: CommentRepository

    // TODO
    private fun accessToken(email: String, role: String) =
        "Bearer " + jwtService.accessToken(email, 1000 * 60, arrayOf(role))
    private fun accessToken() = accessToken("user@example.com", "ROLE_${Role.USER}")

    @Test
    fun `get all posts`() {
        val postsFlow = Flux.just("Post one", "Post two")
            .map {
                Post(title = it, content = "content of $it")
            }
            .asFlow()

        given(posts.findAll()).willReturn(postsFlow)

        client.get().uri("/posts")
            .header(HttpHeaders.AUTHORIZATION, accessToken())
            .exchange()
            .expectStatus()
            .isOk

        verify(posts, times(1)).findAll()

        verifyNoMoreInteractions(posts)
    }

}
