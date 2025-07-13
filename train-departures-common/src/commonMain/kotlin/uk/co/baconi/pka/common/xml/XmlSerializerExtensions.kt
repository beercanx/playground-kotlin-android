package uk.co.baconi.pka.common.xml

import uk.co.baconi.pka.common.AccessToken

fun XmlSerializer.tag(name: String, namespace: String? = null, inner: XmlSerializer.() -> Unit = {}) {
    startTag(namespace, name)
    inner()
    endTag(namespace, name)
}

fun XmlSerializer.attribute(name: String, value: String) {
    attribute(null, name, value)
}

fun XmlSerializer.build(body: XmlSerializer.() -> Unit): String = use {
    startDocument("utf-8", null)
    body()
    endDocument()
    return getOutput()
}

fun XmlSerializer.soapRequest(accessToken: AccessToken, body: XmlSerializer.() -> Unit) {

    val soap = "soap"
    val types = "typ"
    val ldb = "ldb"

    tag("$soap:Envelope") {

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