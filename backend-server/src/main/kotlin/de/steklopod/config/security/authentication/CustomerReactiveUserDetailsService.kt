package de.steklopod.config.security.authentication

import de.steklopod.service.CustomerService
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomerReactiveUserDetailsService(private val customerService: CustomerService) : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> = mono {
        val customer = customerService.findByEmail(username!!) ?: throw BadCredentialsException("Invalid Credentials")
        return@mono User(customer.email, customer.password, listOf(customer))
    }
}
