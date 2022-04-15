package de.steklopod.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Post(
    @Id
    val id: String? = null,
    val title: String,
    val content: String,

    @CreatedDate
    val createdAt: LocalDateTime? = null,
    @LastModifiedDate
    val updatedAt: LocalDateTime? = null
)
