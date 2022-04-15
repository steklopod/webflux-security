package de.steklopod.config.exception

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(PostNotFoundException::class)
    @ResponseStatus(code = NOT_FOUND)
    suspend fun handle(ex: PostNotFoundException, exchange: ServerWebExchange) {
        exchange.response
            .setComplete().awaitFirstOrNull()
        // TODO
    }

    @ExceptionHandler(BadCredentialsException::class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    suspend fun handle(ex: BadCredentialsException, exchange: ServerWebExchange) {
        exchange.response
            .setComplete().awaitFirstOrNull()
    }

}
