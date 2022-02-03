package com.alex.eyk.bot.processor.xml

import com.alex.eyk.bot.processor.xml.exception.MalformedXmlException
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

abstract class AbstractXmlParser<T> : XmlParser<T> {

    override fun parse(inputStream: InputStream): T {
        val saxParser = SAXParserFactory.newInstance()
            .newSAXParser()
        val saxEventHandler = createSaxEventHandler()
        try {
            saxParser.parse(inputStream, saxEventHandler)
            return saxEventHandler.requireResult()
        } catch (e: ParserConfigurationException) {
            throw IllegalStateException("Exception with parser configurator while parsing xml", e)
        } catch (e: IOException) {
            throw IllegalStateException("Exception while read xml input stream", e)
        } catch (e: SAXException) {
            throw MalformedXmlException(e)
        }
    }

    protected abstract fun createSaxEventHandler(): AbstractSaxEventHandler<T>

    abstract class AbstractSaxEventHandler<T> : DefaultHandler() {

        private var result: T? = null

        fun requireResult(): T {
            if (result != null) {
                return result!!
            } else {
                throw IllegalStateException(
                    "Result of parsing: null, make sure that method setResult(T result) was " +
                            "called and parsing has been completed by the time of the call"
                )
            }
        }

        protected fun setResult(result: T?) {
            this.result = result
        }
    }

}