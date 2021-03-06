package de.steklopod.service

import de.steklopod.model.Customer
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class TestUserInitializer(
    private val customerService: CustomerService,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${app.first_user.username}") val firstUsername: String,
    @Value("\${app.first_user.password}") val firstPassword: String
) {
    @EventListener(ApplicationReadyEvent::class)
    fun init() {
        runBlocking {
            val firstCustomer = customerService.findByEmail(firstUsername)

            if (null == firstCustomer) {
                val customer = Customer(
                    UUID.randomUUID().toString(),
                    firstUsername,
                    passwordEncoder.encode(firstPassword)
                )
                customerService.save(customer)

                log.info("First customer created: $firstUsername")
            } else {
                log.info("First customer already created")
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
