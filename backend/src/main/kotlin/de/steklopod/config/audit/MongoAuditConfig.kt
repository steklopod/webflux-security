package de.steklopod.config.audit

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.userdetails.UserDetails
import reactor.core.publisher.Mono

@Configuration
@EnableReactiveMongoAuditing
class MongoAuditConfig {
    @Bean
    fun reactiveAuditorAware() = ReactiveAuditorAware {
        ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .map(Authentication::getPrincipal)
            .map(UserDetails::class.java::cast)
            .map(UserDetails::getUsername)
            .switchIfEmpty(Mono.empty())
    }
}

