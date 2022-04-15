package de.steklopod.controller


import de.steklopod.AppTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders

class PostControllerTests : AppTest()  {

    @Test
    fun `get all posts test`() {
        webTestClient.get().uri("/posts")
            .header(HttpHeaders.AUTHORIZATION, accessToken())
            .exchange()
            .expectStatus()
            .isOk
    }

}
