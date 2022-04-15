package de.steklopod.controller

import de.steklopod.AppTest
import de.steklopod.model.Customer
import de.steklopod.service.CustomerService
import de.steklopod.TestHelper
import de.steklopod.model.Role.ADMIN
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.web.reactive.server.expectBodyList

internal class AdminControllerTest(@Autowired private val customerService: CustomerService) : AppTest() {

    private fun adminAccessToken() = accessToken("user@example.com", "ROLE_$ADMIN")

    @Test
    fun `Given a customer with USER role when tries to fetch data from admin customers API then receives an UNAUTHORIZED error`() {
        client
                .get().uri("/admin/customers")
                .header(HttpHeaders.AUTHORIZATION, accessToken())
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `Given a customer with ADMIN role when tries to fetch data from admin customers API with AUTHORIZATION then receives the data`() {
        runBlocking {
            val customer = TestHelper.randomCustomer()

            customerService.save(customer)

            client
                    .get().uri("/admin/customers")
                    .header(HttpHeaders.AUTHORIZATION, adminAccessToken())
                    .exchange()
                    .expectStatus().isOk
                    .expectBodyList<Customer>()
                    .contains(customer)
        }
    }
}
