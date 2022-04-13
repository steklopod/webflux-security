package de.steklopod.controller

import de.steklopod.service.CustomerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminCustomerController(private val customerService: CustomerService) {

    @GetMapping("/customers")
    fun findAll() = customerService.all()
}
