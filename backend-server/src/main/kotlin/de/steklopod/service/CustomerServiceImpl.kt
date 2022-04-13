package de.steklopod.service

import de.steklopod.model.Customer
import de.steklopod.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(private val customerRepository: CustomerRepository) : CustomerService {

    override fun all(): Flow<Customer> = customerRepository.findAll().asFlow()

    override suspend fun save(customer: Customer): Customer =
        customerRepository.insert(customer).awaitSingle()

    override suspend fun findByEmail(email: String): Customer? =
        customerRepository.findCustomerByEmail(email).awaitFirstOrNull()
}
