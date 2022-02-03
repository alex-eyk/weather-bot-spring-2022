package com.alex.eyk.xml

import java.io.InputStream

interface XmlParser<T> {

    fun parse(inputStream: InputStream): T

}