package de.welcz.hipstertdd

import arrow.core.raise.either
import de.welcz.hipstertdd.helpers.extractNumberFromPath
import de.welcz.hipstertdd.helpers.extractNumberFromQuery
import de.welcz.hipstertdd.helpers.foldServerResponse
import de.welcz.hipstertdd.helpers.responseOk
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class FizzBuzzHandler(private val calculator: Calculator) {
  suspend fun calculateSingleNumber(req: ServerRequest) = either {
    val input = req.extractNumberFromPath("input").bind()
    val calculated = calculator.single(input)
    calculated
  }.foldServerResponse { it.responseOk() }

  suspend fun calculateNumberSequence(req: ServerRequest) = either {
    val limit = req.extractNumberFromQuery("limit", 100).bind()
    val calculated = calculator.sequenceUpTo(limit)
    calculated
  }.foldServerResponse { it.responseOk() }
}
