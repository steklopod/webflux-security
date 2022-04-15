package de.steklopod.config.security

import com.auth0.jwt.interfaces.DecodedJWT
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class JwtTokenReactFilter(private val jwtService: JwtService) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers.getFirst(AUTHORIZATION)
            ?: return chain.filter(exchange)

        if (!authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange)
        }
        try {
            val token: DecodedJWT = jwtService.decodeAccessToken(authHeader)
            log.info("🍄🍄🍄 DecodedJWT: $token")
            val auth = UsernamePasswordAuthenticationToken(
                token.subject,
                null,
                jwtService.getRoles(token)
            )
            return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
        } catch (e: Exception) {
            log.error("JWT exception", e)
        }
        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.clearContext())
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
