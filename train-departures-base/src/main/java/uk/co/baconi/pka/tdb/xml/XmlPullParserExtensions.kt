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

fun XmlPullParser.readAsText(): String? {
    var result: String? = null
    if (this.next() == XmlPullParser.TEXT) {
        result = this.text
        this.nextTag()
    }
    return result
}

fun XmlPullParser.readAsBoolean(): Boolean? {
    return this.readAsText()?.toBoolean()
}

fun XmlPullParser.readAsInt(): Int? {
    return this.readAsText()?.toIntOrNull()
}