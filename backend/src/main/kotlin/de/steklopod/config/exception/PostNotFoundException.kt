package de.steklopod.config.exception

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = NOT_FOUND)
data class PostNotFoundException(val postId: String) : RuntimeException("Post:$postId is not found...")
