package com.alex.eyk.bot.weather.telegram.xml

import com.alex.eyk.bot.weather.telegram.xml.exception.MalformedXmlException
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

abstract class AbstractXmlParser<T> : XmlParser<T> {

    /**
     * @throws MalformedXmlException Будет брошено в случае ошибки в XML-файле
     * @throws IllegalStateException Будет брошено в случае ошибки с конфигурацией SAX-парсера или в случае ошибки при
     *                               чтении файла
     */
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

        /**
         * @return Резлультат парсинга XML-файла в объект
         * @throws IllegalStateException Будет брошено в случае, если результат будет равняться null. Это может
         * произойти в случае, если парсинг прошел некоректно или метод setResult(T result) не был вызван.
         */
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