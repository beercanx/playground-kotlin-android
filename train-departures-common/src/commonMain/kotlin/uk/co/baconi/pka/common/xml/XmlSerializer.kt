package uk.co.baconi.pka.common.xml

import java.io.Closeable

expect class XmlSerializer() : Closeable {
    fun startTag(namespace: String?, name: String): XmlSerializer
    fun endTag(namespace: String?, name: String): XmlSerializer
    fun attribute(namespace: String?, name: String, value: String): XmlSerializer
    fun text(text: String) : XmlSerializer
    fun startDocument(encoding: String?, standalone: Boolean?)
    fun endDocument()
    fun getOutput(): String
}