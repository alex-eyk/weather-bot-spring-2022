package com.alex.eyk.bot.weather.core.xml

import java.io.InputStream

interface XmlParser<T> {

    fun parse(inputStream: InputStream): T

}