package de.steklopod.config.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.server.ResponseStatusException

// TODO: separate exceptions
object HttpExceptionFactory {
    fun badRequest(): ResponseStatusException = ResponseStatusException(BAD_REQUEST, "Bad Request")

    fun unauthorized(): ResponseStatusException = ResponseStatusException(UNAUTHORIZED, "Unauthorized")
}
