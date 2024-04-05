package de.welcz.hipstertdd

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.right
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull

suspend fun <T> Either<RequestError, T>.foldServerResponse(ifRight: suspend (T) -> ServerResponse) =
  fold({ it.responseError() }, { ifRight(it) })

fun ServerRequest.extractNumberFromPath(pathVariable: String) = either {
  val input = pathVariable(pathVariable).toIntOrNull()
  ensureNotNull(input) { InvalidNumber(pathVariable) }
}

suspend fun Any.responseOk() = ServerResponse.ok().bodyValueAndAwait(this)

private suspend fun RequestError.responseError() = when (this) {
  is InvalidNumber -> ServerResponse.badRequest().bodyValueAndAwait(this)
}

data class InvalidNumber(val pathVariable: String) : RequestError("not a valid number")
sealed class RequestError(@Suppress("unused") val message: String)

fun ServerRequest.extractNumberFromQuery(queryVariable: String, defaultValue: Int) = either {
  val limit = queryParamOrNull(queryVariable) ?: return defaultValue.right()
  val limitAsNumber = limit.toIntOrNull()
  ensureNotNull(limitAsNumber) { InvalidNumber(queryVariable) }
}