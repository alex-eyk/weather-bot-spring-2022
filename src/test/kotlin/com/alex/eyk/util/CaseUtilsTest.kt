package com.alex.eyk.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CaseUtilsTest {

    @Test
    fun convertSnakeCaseToClassNameTestWithDividers() {
        assertClassName("TestString", "test_string")
    }

    @Test
    fun convertSnakeCaseToClassNameTestWithoutDividers() {
        assertClassName("Test", "test")
    }

    @Test
    fun convertSnakeCaseToPropertyNameTestWithDividers() {
        assertPropertyName("testString", "test_string")
    }

    @Test
    fun convertSnakeCaseToPropertyNameTestWithoutDividers() {
        assertPropertyName("test", "test")
    }

    private fun assertClassName(expected: String, param: String) {
        val result = CaseUtils
            .convertSnakeCaseToClassName(param)
        assertEquals(expected, result)
    }

    private fun assertPropertyName(expected: String, param: String) {
        val result = CaseUtils
            .convertSnakeCaseToPropertyName(param)
        assertEquals(expected, result)
    }
}