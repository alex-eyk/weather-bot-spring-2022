package com.alex.eyk.xml.impl

import com.alex.eyk.xml.AbstractXmlParser
import org.xml.sax.Attributes

class KeysParser : AbstractXmlParser<Pair<Set<String>, Set<String>>>() {

    override fun createSaxEventHandler(): AbstractSaxEventHandler<Pair<Set<String>, Set<String>>> =
        ReplyMessageSaxEventHandler()

    internal class ReplyMessageSaxEventHandler : AbstractSaxEventHandler<Pair<Set<String>, Set<String>>>() {

        private lateinit var replyKeys: MutableSet<String>
        private lateinit var wordKeys: MutableSet<String>

        private var key: String? = null

        override fun startDocument() {
            this.replyKeys = HashSet()
            this.wordKeys = HashSet()
        }

        override fun startElement(
            uri: String?, localName: String?, qName: String, attributes: Attributes
        ) {
            if (qName == "reply" || qName == "word") {
                this.key = attributes.getKey()
            }
        }

        override fun endElement(uri: String?, localName: String?, qName: String) {
            if (qName == "reply") {
                if (this.key != null) {
                    this.replyKeys.add(this.key!!)
                }
                this.key = null
            } else if (qName == "word") {
                if (this.key != null) {
                    this.wordKeys.add(this.key!!)
                }
                this.key = null
            }
        }

        override fun endDocument() {
            super.setResult(Pair(replyKeys, wordKeys))
        }
    }

}