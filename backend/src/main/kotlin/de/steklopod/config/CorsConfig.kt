package de.steklopod.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class CorsConfig : WebFluxConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedMethods("*")
//          .allowedOrigins("*").allowedHeaders("*") // todo check if these lines are needed
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = CorsConfiguration().let {
        it.allowedOrigins = listOf("http://localhost:3000")
        it.allowedMethods = listOf("GET", "POST")
        UrlBasedCorsConfigurationSource().apply { registerCorsConfiguration("/**", it) }
    }
}
