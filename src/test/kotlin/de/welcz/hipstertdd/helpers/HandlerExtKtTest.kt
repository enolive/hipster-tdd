package de.welcz.hipstertdd.helpers

import de.welcz.hipstertdd.TestRouter
import de.welcz.hipstertdd.shouldHaveJsonBody
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
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
  }
})