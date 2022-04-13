package de.steklopod.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import java.time.Instant

@Document
data class Customer(
    @Id val id: String,
    @Indexed(unique = true)
    val email: String,
    val password: String,
    val role: Role = Role.USER,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val updatedAt: Instant = Instant.now()
) : GrantedAuthority {

    override fun equals(other: Any?) = other is Customer && EssentialCustomerData(this) == EssentialCustomerData(other)
    override fun hashCode() = EssentialCustomerData(id).hashCode()

    override fun getAuthority(): String = "ROLE_$role"


    private data class EssentialCustomerData(val id: String) {
        constructor(customer: Customer) : this(customer.id)
    }
}
