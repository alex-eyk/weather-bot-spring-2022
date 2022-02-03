package com.alex.eyk.bot.processor.xml.impl

import com.alex.eyk.bot.processor.xml.AbstractXmlParser
import com.alex.eyk.bot.processor.xml.impl.AttributesExt.getKey
import org.xml.sax.Attributes

class KeysParser : AbstractXmlParser<Set<String>>() {

    override fun createSaxEventHandler(): AbstractSaxEventHandler<Set<String>> = ReplyMessageSaxEventHandler()

    internal class ReplyMessageSaxEventHandler : AbstractSaxEventHandler<Set<String>>() {

        private lateinit var keys: MutableSet<String>

        private var key: String? = null

        override fun startDocument() {
            this.keys = HashSet()
        }

        override fun startElement(
            uri: String?, localName: String?, qName: String, attributes: Attributes
        ) {
            if (qName == "reply") {
                this.key = attributes.getKey()
            }
        }

        override fun characters(ch: CharArray, start: Int, length: Int) {
        }

        override fun endElement(uri: String?, localName: String?, qName: String) {
            if (qName == "reply") {
                if (this.key != null) {
                    this.keys.add(this.key!!)
                }
                this.key = null
            }
        }

        override fun endDocument() {
            super.setResult(keys)
        }
    }

}

object AttributesExt {

    private const val ATTR_KEY = "key"

    fun Attributes.getKey(): String {
        val key = this.getValue(ATTR_KEY)
        if (key != null) {
            return key
        } else {
            throw IllegalStateException("Attribute `key` should be define for all replies")
        }
    }
}