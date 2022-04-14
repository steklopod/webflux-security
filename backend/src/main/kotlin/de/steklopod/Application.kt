package de.steklopod

import de.steklopod.repository.PostRepository
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.stereotype.Component

@EnableReactiveMongoRepositories
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}


@Component
class DataInitializer(private val postRepository: PostRepository) {

    @EventListener(value = [ApplicationReadyEvent::class])
    fun init() {
        println("🚀 start data initialization  ...")

        runBlocking {
            val deleted = postRepository.deleteAll()
            println(" $deleted posts removed.")
            postRepository.init()
        }
        println(" done data initialization  ...")
    }

}
