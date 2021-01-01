package com.example.android.todoapp.util

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import org.junit.Assert.assertEquals

class CalculatorUtilTest : Spek({
    given("a calculator") {
        val calculator = CalculatorUtil()
        on("addition") {
            val sum = calculator.sum(4, 4)
            it("Should return the result of adding the first number and the second number") {
                assertEquals(8, sum)
            }
        }
        on("subtraction") {
            val sub = calculator.subtract(4, 2)
            it("Should return 2") {
                assertEquals(2, sub)
            }
        }
    }
})