package com.alex.eyk.xml

import java.io.File

interface XmlParser<T> {

    fun parse(file: File): T

}