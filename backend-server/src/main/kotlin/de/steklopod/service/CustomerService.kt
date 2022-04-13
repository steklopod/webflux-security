package de.steklopod.service

import de.steklopod.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerService {
    suspend fun save(customer: Customer): Customer

    fun all(): Flow<Customer>

    suspend fun findByEmail(email: String): Customer?
}
