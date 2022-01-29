package com.alex.eyk.bot.weather.telegram.xml

import java.io.InputStream

interface XmlParser<T> {

    fun parse(xmlStream: InputStream): T

}