package de.steklopod.config

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.server.ResponseStatusException

object HttpExceptionFactory {
    fun badRequest(): ResponseStatusException = ResponseStatusException(BAD_REQUEST, "Bad Request")

    fun unauthorized(): ResponseStatusException = ResponseStatusException(UNAUTHORIZED, "Unauthorized")
}
