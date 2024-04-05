package de.welcz.hipstertdd

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.nulls.shouldNotBeNull
import org.intellij.lang.annotations.Language
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

fun WebTestClient.ResponseSpec.shouldHaveJsonBody(@Language("json") expectedJson: String) {
  expectBody<String>().consumeWith {
    it.responseBody.shouldNotBeNull() shouldEqualJson expectedJson
  }
}