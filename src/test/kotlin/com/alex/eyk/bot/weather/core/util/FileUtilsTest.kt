package com.alex.eyk.bot.weather.core.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class FileUtilsTest {

    @Test
    fun scanResourceDirectoryTest() {
        val files = ResourceUtils.scanResourceDirectory("dictionary")
        for (file in files) {
            if (file.name == "dictionary.ru.xml") {
                return
            }
        }
        Assertions.fail<Void>("File dictionary.ru.xml not found")
    }

}