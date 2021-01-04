package com.example.android.todoapp.util

import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class CalculatorSpecificationStyleTest : Spek({
    describe("A Calculator") {
        val calculator by memoized { CalculatorUtil() }

        context("Addition") {
            var result = 0
            beforeEachTest {
                result = calculator.sum(10, 20)
            }
            it("Should return the result of addition 10 & 20") {
                assertEquals(30, result)
            }
        }

        context("Subtraction") {
            var result = 0
            beforeEachTest {
                result = calculator.subtract(20, 10)
            }
            it("Should return the result of difference of 20 & 10") {
                assertEquals(10, result)
            }
        }

        describe("Multiplication") {
            var result = 0
            beforeEachTest {
                result = calculator.multiple(10, 5)

            }
            it("Should return the result of multiplication of 10 & 5") {
                assertEquals(50, result)
            }
        }

        describe("Division") {
            var result = 0
            beforeEachTest {
                result = calculator.divide(10, 5)
            }
            it("Should return the result of division of 10 & 5") {
                assertEquals(2, result)
            }
        }
    }
})