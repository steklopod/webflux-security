package de.steklopod

import de.steklopod.model.Customer
import java.util.UUID.randomUUID

internal object TestHelper {

    fun randomCustomer() = randomUUID().toString().let {
        Customer(
            id = it.take(4),
            password = "SECRET-$it",
            email = "${it.substring(4, 4)}@example.com"
        )
    }
}
