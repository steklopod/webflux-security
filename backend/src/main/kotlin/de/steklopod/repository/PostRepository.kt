package de.steklopod.repository

import de.steklopod.config.exception.PostNotFoundException
import de.steklopod.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations
import org.springframework.data.mongodb.core.allAndAwait
import org.springframework.data.mongodb.core.asType
import org.springframework.data.mongodb.core.awaitOne
import org.springframework.data.mongodb.core.findReplaceAndAwait
import org.springframework.data.mongodb.core.flow
import org.springframework.data.mongodb.core.insert
import org.springframework.data.mongodb.core.oneAndAwait
import org.springframework.data.mongodb.core.query
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.remove
import org.springframework.data.mongodb.core.update
import org.springframework.stereotype.Component


@Component
class PostRepository(private val mongo: ReactiveFluentMongoOperations) {

    suspend fun count(): Long = mongo.query<Post>().count().awaitSingle()

    fun findAll(): Flow<Post> = mongo.query<Post>().flow()

    suspend fun findOne(id: String): Post = mongo.query<Post>()
        .matching(Query.query(Criteria.where("id").isEqualTo(id))).awaitOne() ?: throw PostNotFoundException(id)

    suspend fun deleteAll(): Long = mongo.remove<Post>().allAndAwait().deletedCount

    suspend fun save(post: Post): Post = mongo.insert<Post>().oneAndAwait(post)

    suspend fun update(post: Post) = mongo.update<Post>()
        //  .matching(query(where("id").isEqualTo(id)))
        //  .apply(Update.update("title",post.title!!).set("content", post.content!!))
        .replaceWith(post)
        .asType<Post>().findReplaceAndAwait()

    suspend fun init() {
        save(Post(title = "My first post title", content = "Content of my first post"))
        save(Post(title = "My second post title", content = "Content of my second post"))
    }
}
