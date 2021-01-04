package com.example.android.todoapp.util

import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class CalculatorGherkinStyleTest : Spek({
    Feature("Calculator") {
        val calculator = CalculatorUtil()

        Scenario("Addition") {
            var num1 = 0
            var num2 = 0
            var sum = 0
            val result = 20
            Given("Initialize num1 and num2") {
                num1 = 10
                num2 = 10
            }
            When("Add num1 and num2") {
                sum = calculator.sum(num1, num2)
            }
            Then("Should return addition of num1 and num2") {
                assertEquals(result, sum)
            }
        }

        Scenario("Subtraction") {
            var num1 = 0
            var num2 = 0
            var sub = 0
            val result = 30
            Given("Initialize num1 and num2") {
                num1 = 40
                num2 = 10
            }
            When("Subtract num2 from num1") {
                sub = calculator.subtract(num1, num2)
            }
            Then("Should return difference value of two num1 and num2") {
                assertEquals(result, sub)
            }
        }

        Scenario("Multiplication") {
            var num1 = 0
            var num2 = 0
            var multiple = 0
            val result = 25
            Given("Initialize num1 and num2") {
                num1 = 5
                num2 = 5
            }
            When("Multiplication of num1 and num2") {
                multiple = calculator.multiple(num1, num2)
            }

            Then("Should return result of multiplication of num1 and num2") {
                assertEquals(result, multiple)
            }
        }

        Scenario("Division") {
            var num1 = 0
            var num2 = 0
            var divide = 0
            val result = 2
            Given("Initialize num 1 and num2") {
                num1 = 4
                num2 = 2
            }
            When("Division of num1 by num2") {
                divide = calculator.divide(num1, num2)
            }
            Then("Should return result of division of num1 by num2") {
                assertEquals(result, divide)
            }
        }
    }
})