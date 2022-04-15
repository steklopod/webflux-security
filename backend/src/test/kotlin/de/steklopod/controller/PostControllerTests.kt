package de.steklopod.controller


import de.steklopod.AppTest
import de.steklopod.model.Customer
import de.steklopod.model.Post
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.reactive.server.expectBodyList

class PostControllerTests : AppTest()  {

    @Test
    fun `get all posts test`() {
        webTestClient.get().uri("/posts")
            .header(HttpHeaders.AUTHORIZATION, accessToken())
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList<Post>()
    }

}
