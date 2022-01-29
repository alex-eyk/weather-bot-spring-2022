package com.alex.eyk.bot.weather.core.xml.impl

import com.alex.eyk.bot.weather.core.xml.AbstractXmlParser
import org.xml.sax.Attributes
import org.xml.sax.SAXException

class ReplyXmlParser : AbstractXmlParser<Map<String, String>>() {

    override fun createSaxEventHandler(): AbstractSaxEventHandler<Map<String, String>> = ReplyMessageSaxEventHandler()

    internal class ReplyMessageSaxEventHandler : AbstractSaxEventHandler<Map<String, String>>() {

        private lateinit var keyToMessageMap: MutableMap<String, String>

        /**
         * Текущий ключ для получения сообщения. Может быть равен null в случае, если в данный момент времени не
         * рассматривается необходимый элемент `reply`
         */
        private var key: String? = null

        /**
         * Текущее сообщение для ключа. Может быть равен null в случае, если в данный момент времени не
         * рассматривается необходимый элемент `reply`
         */
        private var value: String? = null

        override fun startDocument() {
            this.keyToMessageMap = HashMap()
        }

        override fun startElement(
            uri: String?, localName: String?, qName: String, attributes: Attributes
        ) {
            if (qName == "reply") {
                val key = attributes.getValue("key")
                if (key != null) {
                    this.key = key
                } else {
                    throw SAXException("No value for param 'key'")
                }
            }
        }

        override fun characters(ch: CharArray, start: Int, length: Int) {
            if (this.key != null) {
                val read = String(ch, start, length)
                if (this.value != null) {
                    this.value += read
                } else {
                    this.value = read
                }
            }
        }

        override fun endElement(uri: String?, localName: String?, qName: String) {
            if (qName == "reply") {
                if (this.key != null && this.value != null) {
                    keyToMessageMap[this.key!!] = this.value!!
                }
                this.key = null
                this.value = null
            }
        }

        override fun endDocument() {
            super.setResult(keyToMessageMap)
        }
    }

}