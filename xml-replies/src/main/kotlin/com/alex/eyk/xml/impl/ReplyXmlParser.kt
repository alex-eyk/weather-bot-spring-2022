package com.alex.eyk.xml.impl

import com.alex.eyk.entity.Reply
import com.alex.eyk.xml.AbstractXmlParser
import org.xml.sax.Attributes

class ReplyXmlParser : AbstractXmlParser<Map<String, Reply>>() {

    override fun createSaxEventHandler(): AbstractSaxEventHandler<Map<String, Reply>> = ReplyMessageSaxEventHandler()

    internal class ReplyMessageSaxEventHandler : AbstractSaxEventHandler<Map<String, Reply>>() {

        private lateinit var repliesMap: MutableMap<String, Reply>

        private var key: String? = null
        private var content: String? = null
        private var format: Boolean = false
        private var markdown: Boolean = false

        override fun startDocument() {
            this.repliesMap = HashMap()
        }

        override fun startElement(
            uri: String?, localName: String?, qName: String, attributes: Attributes
        ) {
            if (qName == "reply") {
                this.key = attributes.getKey()
                this.format = attributes.getFormat(format)
                this.markdown = attributes.getMarkdown(markdown)
            }
        }

        override fun characters(ch: CharArray, start: Int, length: Int) {
            if (this.key != null) {
                val read = String(ch, start, length)
                if (this.content != null) {
                    this.content += read
                } else {
                    this.content = read
                }
            }
        }

        override fun endElement(uri: String?, localName: String?, qName: String) {
            if (qName == "reply") {
                if (this.key != null && this.content != null) {
                    if (this.repliesMap.contains(this.key)) {
                        throw IllegalStateException(
                            "Each key should be uniq. Replies xml file contains more " +
                                    "than one reply whit key: $key"
                        )
                    }
                    repliesMap[this.key!!] = Reply(this.content!!, format, markdown)
                }
                this.key = null
                this.content = null
                this.format = false
                this.markdown = false
            }
        }

        override fun endDocument() {
            super.setResult(repliesMap)
        }
    }

}