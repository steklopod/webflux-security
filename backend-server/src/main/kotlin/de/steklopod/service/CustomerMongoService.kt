package de.steklopod.service

import de.steklopod.model.Customer
import de.steklopod.repository.CustomerSpringMongoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Service

@Service
class CustomerMongoService(private val customerSpringMongoRepository: CustomerSpringMongoRepository) :
    CustomerService {

    override suspend fun save(customer: Customer): Customer =
        customerSpringMongoRepository.insert(customer).awaitSingle()

    override fun all(): Flow<Customer> = customerSpringMongoRepository.findAll().asFlow()

    override suspend fun findByEmail(email: String): Customer? =
        customerSpringMongoRepository.findCustomerByEmail(email).awaitFirstOrNull()
}
