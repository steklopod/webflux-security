package de.steklopod

import de.steklopod.model.Customer
import java.util.UUID

object CustomerHelper {

    fun random() = Customer(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString() + "@example.com",
            UUID.randomUUID().toString()
    )
}
