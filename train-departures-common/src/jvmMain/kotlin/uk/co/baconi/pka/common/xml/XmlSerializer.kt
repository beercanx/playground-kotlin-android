package uk.co.baconi.pka.common.xml

import java.io.StringWriter
import java.io.Writer
import org.xmlpull.v1.XmlSerializer as XmlPullSerializer

actual class XmlSerializer {

    private val writer: Writer = StringWriter()
    private val delegate: XmlPullSerializer = XmlParserFactory.serializer().apply {
        setOutput(writer)
    }

    actual fun startTag(namespace: String?, name: String): XmlSerializer {
        delegate.startTag(namespace, name)
        return this
    }

    actual fun endTag(namespace: String?, name: String): XmlSerializer {
        delegate.endTag(namespace, name)
        return this
    }

    actual fun attribute(namespace: String?, name: String, value: String): XmlSerializer {
        delegate.attribute(namespace, name, value)
        return this
    }

    actual fun text(text: String): XmlSerializer {
        delegate.text(text)
        return this
    }

    actual fun startDocument(encoding: String?, standalone: Boolean?) {
        delegate.startDocument(encoding, standalone)
    }

    actual fun endDocument() {
        delegate.endDocument()
    }

    actual fun getOutput(): String {
        return writer.toString()
    }
}