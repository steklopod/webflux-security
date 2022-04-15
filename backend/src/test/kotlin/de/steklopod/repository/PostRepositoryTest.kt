package de.steklopod.repository

import de.steklopod.model.Post
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations


@DataMongoTest
class PostRepositoryTest(@Autowired private val mongo: ReactiveFluentMongoOperations) {
    private val posts: PostRepository = PostRepository(mongo)

    @Test
    fun `get all posts test`() {
        runBlocking {

            val inserted: Post = posts.save(Post(title = "mytitle", content = "mycontent"))

            assertNotNull(inserted.id)
            println("inserted id: $inserted.id")

            val post = posts.findOne(inserted.id!!)
            assertEquals("mytitle", post.title)
            assertEquals("mycontent", post.content)
        }

    }


}
