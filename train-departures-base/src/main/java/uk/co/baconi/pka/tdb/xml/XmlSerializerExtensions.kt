package uk.co.baconi.pka.tdb.xml

import org.xmlpull.v1.XmlSerializer
import java.io.StringWriter

fun XmlSerializer.tag(namespace: String?, name: String, inner: XmlSerializer.() -> Unit = {}) {
    startTag(namespace, name)
    inner()
    endTag(namespace, name)
}

fun XmlSerializer.attribute(name: String, value: String) {
    attribute(null, name, value)
}

fun XmlSerializer.tag(name: String, inner: XmlSerializer.() -> Unit) {
    tag(null, name, inner)
}

fun XmlSerializer.build(body: XmlSerializer.() -> Unit): String {

    val writer = StringWriter()
    setOutput(writer)

    startDocument("utf-8", null)

    body()

    endDocument()

    return writer.toString()

}