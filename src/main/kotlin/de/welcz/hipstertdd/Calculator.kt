package de.welcz.hipstertdd

import org.springframework.stereotype.Service

@Service
class Calculator {
  private val rules = listOf(
    DivByRule(3, "Fizz"),
    DivByRule(5, "Buzz"),
  )

  fun single(input: Int) =
    rules
      .filter { it.appliesTo(input) }
      .joinToString("-") { it.result }
      .takeIf { it.isNotEmpty() }
      ?: input.toString()

  fun sequenceUpTo(limit: Int) =
    (1..limit).map { single(it) }


  data class DivByRule(val multiple: Int, val result: String) {
    fun appliesTo(input: Int) = input % multiple == 0
  }
}
