package com.alex.eyk.bot.processor.xml

import java.io.InputStream

interface XmlParser<T> {

    fun parse(inputStream: InputStream): T

}