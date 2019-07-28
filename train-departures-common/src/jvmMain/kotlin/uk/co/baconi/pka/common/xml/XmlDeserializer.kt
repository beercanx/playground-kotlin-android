package uk.co.baconi.pka.common.xml

import kotlinx.io.core.Closeable
import org.xmlpull.v1.XmlPullParser
import java.io.Reader

actual class XmlDeserializer(reader: Reader, parser: XmlPullParser): XmlPullParser by parser, Closeable by reader {

    actual constructor(input: String) : this(input.reader(), XmlParserFactory.deserializer())

    init {
        parser.setInput(reader)
        parser.nextTag()
    }

    actual fun isStartTag(): Boolean = eventType == XmlPullParser.START_TAG

    actual fun isEndTag(): Boolean = eventType == XmlPullParser.END_TAG

    actual fun isText(): Boolean = eventType == XmlPullParser.TEXT

    actual fun requireStartTag(tag: String) = require(XmlPullParser.START_TAG, null, tag)

    actual fun requireEndTag(tag: String) = require(XmlPullParser.END_TAG, null, tag)

    internal actual fun nextIs(expected: () -> Boolean): Boolean {
        next()
        return expected()
    }
}