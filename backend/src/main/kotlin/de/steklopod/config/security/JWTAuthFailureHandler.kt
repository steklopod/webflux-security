package de.steklopod.config.security

import de.steklopod.config.HttpExceptionFactory
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JWTAuthFailureHandler : ServerAuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange, exception: AuthenticationException?
    ): Mono<Void> = mono {
        val exchange = webFilterExchange.exchange ?: throw HttpExceptionFactory.unauthorized()

        logger.error("Oops: ${UNAUTHORIZED.reasonPhrase}")

        with(exchange.response) {
            statusCode = UNAUTHORIZED
            setComplete().awaitFirstOrNull()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
