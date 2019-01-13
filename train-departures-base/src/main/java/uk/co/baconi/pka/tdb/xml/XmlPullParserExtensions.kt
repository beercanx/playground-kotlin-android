package uk.co.baconi.pka.tdb.xml

import org.xmlpull.v1.XmlPullParser

fun XmlPullParser.skip() {
    if (this.eventType != XmlPullParser.START_TAG) {
        throw IllegalStateException()
    }
    var depth = 1
    while (depth != 0) {
        when (this.next()) {
            XmlPullParser.END_TAG -> depth--
            XmlPullParser.START_TAG -> depth++
        }
    }
}

fun XmlPullParser.readText(): String? {
    var result: String? = null
    if (this.next() == XmlPullParser.TEXT) {
        result = this.text
        this.nextTag()
    }
    return result
}

fun XmlPullParser.readBoolean(): Boolean? {
    return this.readText()?.toBoolean()
}