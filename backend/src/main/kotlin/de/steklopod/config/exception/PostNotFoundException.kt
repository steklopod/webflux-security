package de.steklopod.config.exception

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException

@ResponseStatus(code = NOT_FOUND)
data class PostNotFoundException(val postId: String) :
    ResponseStatusException(NOT_FOUND, "Post: $postId is not found...")
