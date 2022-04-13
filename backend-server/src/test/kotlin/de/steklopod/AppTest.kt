package de.steklopod

import de.steklopod.config.security.JWTService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
     lateinit var jwtService: JWTService

     fun accessToken() = accessToken("user@example.com", "ROLE_USER")
     fun adminAccessToken() = accessToken("user@example.com", "ROLE_ADMIN")

     fun accessToken(email: String, role: String) =
            "Bearer " + jwtService.accessToken(email, 1000 * 60, arrayOf(role))
}
