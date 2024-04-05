package de.welcz.hipstertdd

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Router(private val handler: FizzBuzzHandler) {
  @Bean
  fun routes() = coRouter {
    "/fizz-buzz".nest {
      GET("{input}", handler::calculateSingleNumber)
      GET("", handler::calculateNumberSequence)
    }
  }
}

