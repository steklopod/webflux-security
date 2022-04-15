package de.steklopod.controller

import de.steklopod.service.CustomerService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class CustomerAdminController(private val customerService: CustomerService) {

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customers")
    fun findAll() = customerService.all()
}
