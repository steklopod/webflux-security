package de.steklopod.controller


import de.steklopod.AppTest
import de.steklopod.model.Post
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.expectBodyList

class PostControllerTests : AppTest() {

    @Test
    fun `get all posts test`() {
        client.get()
            .uri("/posts")
            .header(AUTHORIZATION, accessToken())
            .exchange()
            .expectStatus().isOk
            .expectBodyList<Post>()
//            .hasSize(2)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `get all posts test with MOCK USER`() {
        client.get()
            .uri("/posts")
            .exchange()
            .expectStatus().isOk
            .expectBodyList<Post>()
    }


    @Test
    fun `create a post without auth should fail with 401`() {
        client.post()
            .uri("/posts")
            .bodyValue(Post(content = "test post", title = "title_here"))
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    @Disabled
    fun `get none existing post should return 404`() {
        client.get()
            .uri("/posts/notexisted")
            .header(AUTHORIZATION, accessToken())
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    @Disabled
    fun `create a post withs auth should ok`() {
        client.post()
            .uri("/posts")
            .bodyValue(Post(content = "test post", title = "title_here"))
            .headers {
                it.setBearerAuth(accessToken())
                it.contentType = APPLICATION_JSON
            }
            .exchange()
            .expectStatus().isCreated
            .expectHeader().exists("Content-Type")
    }

}
