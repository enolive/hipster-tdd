package de.welcz.hipstertdd

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.azstring
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.random.Random

@WebFluxTest
@Import(Router::class, FizzBuzzHandler::class)
class FizzBuzzHandlerTest(
  private val webTestClient: WebTestClient,
  @MockkBean private val calculator: Calculator,
) : DescribeSpec({
  describe("API for /fizz-buzz") {
    describe("GET /{input}") {
      it("returns calculated fizz-buzz result") {
        val input = Random.nextInt()
        val expectedResult = "Result ${Random.azstring(10)}"
        every { calculator.single(input) } returns expectedResult

        val response = webTestClient.get().uri("/fizz-buzz/$input").exchange()

        response.expectStatus().isOk
        response.expectBody<String>().value { it shouldBe expectedResult }
      }
    }

    describe("GET /") {
      it("returns sequence of fizz-buzz results up to the specified limit") {
        val limit = Random.nextInt()
        val expectedJson = """["First", "Second", "Third"]"""
        every { calculator.sequenceUpTo(limit) } returns listOf("First", "Second", "Third")

        val response = webTestClient.get().uri("/fizz-buzz?limit=$limit").exchange()

        response.expectStatus().isOk
        response.shouldHaveJsonBody(expectedJson)
      }

      it("returns sequence of fizz-buzz results up to the default limit") {
        val expectedJson = """["First", "Second", "Third"]"""
        every { calculator.sequenceUpTo(100) } returns listOf("First", "Second", "Third")

        val response = webTestClient.get().uri("/fizz-buzz").exchange()

        response.expectStatus().isOk
        response.shouldHaveJsonBody(expectedJson)
      }
    }
  }
})

