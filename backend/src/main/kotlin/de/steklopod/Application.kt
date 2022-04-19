package de.steklopod

import de.steklopod.repository.PostRepository
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.stereotype.Component
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@EnableReactiveMongoRepositories
@OpenAPIDefinition(servers = [Server(url = "http://localhost:3000")],
    info = Info(contact = Contact(name = "Dmitry Kaltovich", email = "steklopod@gmail.com", url = "https://github.com/steklopod"),
                description = "Webflux application", title = "Reactive kotlin")
)
@SpringBootApplication
class Application
fun main(args: Array<String>) { runApplication<Application>(*args) }


@Component
class DataInitializer(private val postRepository: PostRepository, private val sp: ServerProperties) {

    @EventListener(value = [ApplicationReadyEvent::class])
    fun init() {
        println("\t📁 API document : http://localhost:${sp.port}/swagger-ui.html \n")
        println("\t🐑 Swagger Yaml : http://localhost:${sp.port}/docs.yaml\n")

        println("\t🚀 start data initialization ...\n")
        runBlocking {
            val deleted = postRepository.deleteAll()
            println("\t[$deleted] posts removed ")
            postRepository.init()
        }
        println("\n\t OK: done data initialization... 🐲\n")
    }

}
