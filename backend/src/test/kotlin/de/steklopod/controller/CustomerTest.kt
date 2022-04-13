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

internal class CustomerTest(@Autowired private val customerService: CustomerService) : AppTest() {

    @Test
    fun `Given a customer when tries to fetch data from customers API without AUTHORIZATION header then receives an UNAUTHORIZED error`() {
        webTestClient
                .get().uri("/v1/customers")
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `Given a customer when tries to fetch data from customers API with AUTHORIZATION header but without starting with Bearer then receives an UNAUTHORIZED error`() {
        webTestClient
                .get().uri("/v1/customers")
                .header(HttpHeaders.AUTHORIZATION, accessToken().replace("Bearer ", ""))
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `Given a customer when tries to fetch data from customers API with AUTHORIZATION header but with a not compliant Bearer token then receives an UNAUTHORIZED error`() {
        webTestClient
                .get().uri("/v1/customers")
                .header(HttpHeaders.AUTHORIZATION, "Bearer test")
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `Given a customer when tries to fetch data from customers API with AUTHORIZATION then receives the data`() {
        runBlocking {
            val customer = CustomerHelper.random()

            customerService.save(customer)

            webTestClient
                    .get().uri("/v1/customers")
                    .header(HttpHeaders.AUTHORIZATION, accessToken())
                    .exchange()
                    .expectStatus().isOk
                    .expectBodyList<Customer>()
                    .contains(customer)
        }
    }
}
