package de.welcz.hipstertdd.helpers

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull

suspend fun <T> Either<RequestError, T>.foldServerResponse(ifRight: suspend (T) -> ServerResponse) =
  fold({ it.responseError() }, { ifRight(it) })

suspend fun Any.responseOk() = ServerResponse.ok().bodyValueAndAwait(this)

suspend fun RequestError.responseError() = when (this) {
  is InvalidNumber -> ServerResponse.badRequest().bodyValueAndAwait(this)
}

sealed class RequestError(@Suppress("unused") val message: String)
data class InvalidNumber(val pathVariable: String) : RequestError("not a valid number")

fun ServerRequest.extractNumberFromPath(pathVariable: String) = either {
  val input = pathVariable(pathVariable).toIntOrNull()
  ensureNotNull(input) { InvalidNumber(pathVariable) }
}

fun ServerRequest.extractNumberFromQuery(queryVariable: String, defaultValue: Int) = either {
  val limit = queryParamOrNull(queryVariable) ?: return@either defaultValue
  val limitAsNumber = limit.toIntOrNull()
  ensureNotNull(limitAsNumber) { InvalidNumber(queryVariable) }
}