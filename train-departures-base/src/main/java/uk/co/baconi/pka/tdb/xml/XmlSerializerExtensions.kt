package uk.co.baconi.pka.tdb.xml

import org.xmlpull.v1.XmlSerializer
import uk.co.baconi.pka.tdb.AccessToken
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

fun XmlSerializer.soapRequest(accessToken: AccessToken, body: XmlSerializer.() -> Unit) {

    val soap = "soap"
    val types = "typ"
    val ldb = "ldb"

    tag("$soap:Envelope") {

        // TODO - Extract into params if required
        attribute("xmlns:$soap", "http://www.w3.org/2003/05/soap-envelope")
        attribute("xmlns:$types", "http://thalesgroup.com/RTTI/2013-11-28/Token/types")
        attribute("xmlns:$ldb", "http://thalesgroup.com/RTTI/2017-10-01/ldb/")

        tag("$soap:Header") {
            tag("$types:AccessToken") {
                tag("$types:TokenValue") {
                    text(accessToken.value)
                }
            }
        }
        tag("$soap:Body") {
            body()
        }
    }
}