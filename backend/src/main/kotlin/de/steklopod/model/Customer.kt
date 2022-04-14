package de.steklopod.model

import de.steklopod.model.Role.USER
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import java.time.Instant
import java.time.LocalDateTime

@Document
data class Customer(
    @Id
    val id: String,

    @Indexed(unique = true)
    val email: String,

    val password: String,

    val role: Role = USER,

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val updatedAt: LocalDateTime? = null
) : GrantedAuthority {

    override fun getAuthority(): String = "ROLE_$role"

    override fun hashCode(): Int = id.hashCode()
    override fun equals(other: Any?): Boolean = other is Customer && other.id == id
}
