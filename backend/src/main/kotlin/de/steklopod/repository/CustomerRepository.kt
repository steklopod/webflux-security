package de.steklopod.repository

import de.steklopod.model.Customer
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface CustomerRepository : ReactiveMongoRepository<Customer, String> {
    fun findCustomerByEmail(email: String): Mono<Customer>
}
