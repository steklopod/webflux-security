package de.steklopod.controller

import de.steklopod.AppTest
import de.steklopod.model.Customer
import de.steklopod.service.CustomerService
import de.steklopod.CustomerHelper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.web.reactive.server.expectBodyList

internal class AdminTest(@Autowired private val customerService: CustomerService) : AppTest() {

    @Test
    fun `Given a customer with USER role when tries to fetch data from admin customers API then receives an UNAUTHORIZED error`() {
        webTestClient
                .get().uri("/admin/customers")
                .header(HttpHeaders.AUTHORIZATION, accessToken())
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `Given a customer with ADMIN role when tries to fetch data from admin customers API with AUTHORIZATION then receives the data`() {
        runBlocking {
            val customer = CustomerHelper.random()

            customerService.save(customer)

            webTestClient
                    .get().uri("/admin/customers")
                    .header(HttpHeaders.AUTHORIZATION, adminAccessToken())
                    .exchange()
                    .expectStatus().isOk
                    .expectBodyList<Customer>()
                    .contains(customer)
        }
    }
}
