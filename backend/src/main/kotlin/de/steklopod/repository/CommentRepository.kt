package de.steklopod.repository

import de.steklopod.model.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations
import org.springframework.data.mongodb.core.flow
import org.springframework.data.mongodb.core.insert
import org.springframework.data.mongodb.core.oneAndAwait
import org.springframework.data.mongodb.core.query
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component


@Component
class CommentRepository(private val mongo: ReactiveFluentMongoOperations) {

    suspend fun save(comment: Comment): Comment = mongo.insert<Comment>().oneAndAwait(comment)

    suspend fun countByPostId(postId: String): Long = mongo.query<Comment>()
        .matching(Query.query(Criteria.where("postId").isEqualTo(postId)))
        .count().awaitSingle()

    fun findByPostId(postId: String): Flow<Comment> = mongo.query<Comment>()
        .matching(Query.query(Criteria.where("postId").isEqualTo(postId))).flow()
}
