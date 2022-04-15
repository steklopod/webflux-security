package de.steklopod.service

import de.steklopod.model.Customer
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(private val customerService: CustomerService) : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> = mono {
        val customer: Customer = customerService.findByEmail(username!!)
            ?: throw BadCredentialsException("Invalid Credentials")

        val authorities: List<GrantedAuthority> = listOf(customer)

        org.springframework.security.core.userdetails.User(
            customer.email,
            customer.password,
            authorities
        )
    }

    // TODO: uncomment for local test only
    //  @Bean
    fun inMemoryUserDetailsService(passwordEncoder: PasswordEncoder): MapReactiveUserDetailsService {
        val admin: UserDetails = org.springframework.security.core.userdetails.User
            .withUsername("admin")
            .password("admin")
            .roles("USER", "ADMIN")
            .passwordEncoder(passwordEncoder::encode)
            .build()
        println("admin: $admin")
        return MapReactiveUserDetailsService(admin)
    }
}
