package de.steklopod.controller

import de.steklopod.AppTest
import de.steklopod.model.LoginRequest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION

internal class LoginControllerTest : AppTest() {

    @Value("\${app.first_user.username}")
    private lateinit var firstUsername: String

    @Value("\${app.first_user.password}")
    private lateinit var firstPassword: String

    @Test
    fun `Given an existing customer when tries to login then get an access and refresh token`() {
        val responseHeaders: HttpHeaders = client
            .post().uri("/login")
            .bodyValue(LoginRequest(firstUsername, firstPassword))
            .exchange()
            .expectStatus().isOk
            .expectHeader().exists(AUTHORIZATION)
            .expectHeader().exists("Refresh-Token")
            .expectBody().returnResult().responseHeaders

        val accessToken = responseHeaders[AUTHORIZATION]?.get(0)
        val refreshToken = responseHeaders["Refresh-Token"]?.get(0)

        val decodedAccessToken = jwtService.decodeAccessToken(accessToken!!)
        jwtService.decodeRefreshToken(refreshToken!!)

        assertTrue(jwtService.getRoles(decodedAccessToken).any { it.authority == "ROLE_USER" })
    }

    @Test
    fun `Given an unknown customer when try to login then receives an UNAUTHORIZED error`() {
        client
            .post().uri("/login")
            .bodyValue(LoginRequest("unknown@example.com", "unknownpassword"))
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `Given a customer when tries to login with a username that is not a valid email then receives UNAUTHORIZED error`() {
        client
            .post().uri("/login")
            .bodyValue(LoginRequest("invalid@asd", "invalid"))
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `Given a customer when tries to login with correct email but not sized password then receives UNAUTHORIZED error`() {
        client
            .post().uri("/login")
            .bodyValue(LoginRequest("invalid@asd.com", "invalid"))
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `Given a customer when tries to login with correct email but incorrect password then receives BAD REQUEST error`() {
        client
            .post().uri("/login")
            .bodyValue(LoginRequest(firstUsername, "invalidpassword"))
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `Given a customer when tries to login with incorrect JSON request then receives BAD REQUEST error`() {
        val badRequest = object {
            val invalidUsername = "invalid@asd.com"
            val password = "invalid"
        }

        client
            .post().uri("/login")
            .bodyValue(badRequest)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `Given a customer when tries to login making a GET request then receives NOT FOUND error`() {
        client
            .get().uri("/login")
            .exchange()
            .expectStatus().isNotFound
    }
}
