package de.welcz.hipstertdd.helpers

import de.welcz.hipstertdd.shouldHaveJsonBody
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.server.coRouter
import kotlin.random.Random

@WebFluxTest
@Import(TestRouter::class)
class HandlerExtKtTest(private val webTestClient: WebTestClient) : DescribeSpec({
  describe("handler extensions") {
    describe("extract number from path") {
      it("returns the number") {
        val input = Random.nextInt()

        val response = webTestClient.get().uri("/extract-from-path/$input").exchange()

        response.expectStatus().isOk
        response.expectBody<Int>().value { it shouldBe input }
      }

      it("rejects input that is not a number") {
        val input = "Not a number"
        val expectedJson = """{"pathVariable":"number","message":"not a valid number"}"""

        val response = webTestClient.get().uri("/extract-from-path/$input").exchange()

        response.expectStatus().isBadRequest
        response.shouldHaveJsonBody(expectedJson)
      }
    }

    describe("extract number from query") {
      it("returns the number") {
        val input = Random.nextInt()

        val response = webTestClient.get().uri("/extract-from-query?number=$input").exchange()

        response.expectStatus().isOk
        response.expectBody<Int>().value { it shouldBe input }
      }

      it("returns the default number if query parameter is missing") {
        val response = webTestClient.get().uri("/extract-from-query").exchange()

        response.expectStatus().isOk
        response.expectBody<Int>().value { it shouldBe 42 }
      }

      it("rejects input that is not a number") {
        val input = "Not a number"
        val expectedJson = """{"pathVariable":"number","message":"not a valid number"}"""

        val response = webTestClient.get().uri("/extract-from-query?number=$input").exchange()

        response.expectStatus().isBadRequest
        response.shouldHaveJsonBody(expectedJson)
      }
    }
  }
})

@TestConfiguration
class TestRouter {
  @Bean
  fun routes() = coRouter {
    GET("/extract-from-path/{number}") {
      it.extractNumberFromPath("number").foldServerResponse { it.responseOk() }
    }
    GET("/extract-from-query") {
      it.extractNumberFromQuery("number", 42).foldServerResponse { it.responseOk() }
    }
  }
}