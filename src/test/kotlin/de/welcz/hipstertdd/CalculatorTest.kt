package de.welcz.hipstertdd

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [Calculator::class])
class CalculatorTest(private val underTest: Calculator) : DescribeSpec({
  describe("calculator for fizz-buzz") {
    describe("single number is correctly calculated") {
      data class TestCase(val input: Int, val expectedOutput: String) : WithDataTestName {
        override fun dataTestName() = "$input is calculated as $expectedOutput"
      }

      withData(
        TestCase(1, "1"),
        TestCase(2, "2"),
        TestCase(3, "Fizz"),
        TestCase(6, "Fizz"),
        TestCase(5, "Buzz"),
        TestCase(10, "Buzz"),
        TestCase(15, "Fizz-Buzz"),
        TestCase(30, "Fizz-Buzz"),
      ) { (input, expectedResult) ->
        underTest.single(input) shouldBe expectedResult
      }
    }

    describe("sequence of numbers is correctly calculated") {
      data class TestCase(val limit: Int, val expectedOutput: List<String>) : WithDataTestName {
        override fun dataTestName() = "$limit -> $expectedOutput"
      }

      withData(
        TestCase(0, emptyList()),
        TestCase(1, listOf("1")),
        TestCase(3, listOf("1", "2", "Fizz")),
        TestCase(
          15,
          listOf("1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8", "Fizz", "Buzz", "11", "Fizz", "13", "14", "Fizz-Buzz")
        ),
      ) { (limit, expectedResult) ->
        underTest.sequenceUpTo(limit) shouldBe expectedResult
      }
    }
  }
})
