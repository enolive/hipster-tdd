package de.welcz.hipstertdd

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class FizzBuzzHandler {
  suspend fun calculateSingleNumber(req: ServerRequest): ServerResponse {
    val input = req.pathVariable("input")
    return ServerResponse.ok().bodyValueAndAwait("Result for $input")
  }
}