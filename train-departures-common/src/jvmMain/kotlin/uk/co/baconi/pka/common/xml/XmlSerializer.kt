package uk.co.baconi.pka.common.xml

import java.io.Closeable
import java.io.StringWriter
import java.io.Writer

import org.xmlpull.v1.XmlSerializer as XmlPullSerializer

actual class XmlSerializer(
    private val writer: Writer,
    private val serializer: XmlPullSerializer
): XmlPullSerializer by serializer, Closeable by writer {

    init {
        serializer.setOutput(writer)
    }

    actual constructor() : this(StringWriter(), XmlParserFactory.serializer())

    actual override fun startTag(namespace: String?, name: String): XmlSerializer =
        serializer.startTag(namespace, name) as XmlSerializer

    actual override fun endTag(namespace: String?, name: String): XmlSerializer =
        serializer.endTag(namespace, name) as XmlSerializer

    actual override fun attribute(namespace: String?, name: String, value: String): XmlSerializer =
        serializer.attribute(namespace, name, value) as XmlSerializer

    actual override fun text(text: String): XmlSerializer =
        serializer.text(text) as XmlSerializer

    actual fun getOutput(): String =
        writer.toString()
}