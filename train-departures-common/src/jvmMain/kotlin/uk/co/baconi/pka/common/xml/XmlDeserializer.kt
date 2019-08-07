package uk.co.baconi.pka.common.xml

import kotlinx.io.core.Closeable
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.Reader

actual class XmlDeserializer(private val parser: XmlPullParser, reader: Reader): Closeable by reader {

    actual constructor(input: String) : this(XmlParserFactory.deserializer(), input.reader())

    init {
        parser.setInput(reader)
        parser.nextTag()
    }

    actual fun require(type: EventType, name: String, namespace: String?) = parser.require(type.int(), namespace, name)
    actual fun getAttributeValue(name: String, namespace: String?): String? = parser.getAttributeValue(namespace, name)
    actual fun getPrefix(): String? = parser.prefix
    actual fun getName(): String? = parser.name
    actual fun next(): EventType = parser.next().eventType()
    actual fun nextTag(): EventType = parser.nextTag().eventType()
    actual fun getText(): String? = parser.text
    actual fun getEventType(): EventType = parser.eventType.eventType()

    private fun Int.eventType(): EventType = when(this) {
        XmlPullParser.START_DOCUMENT -> EventType.START_DOCUMENT
        XmlPullParser.END_DOCUMENT -> EventType.END_DOCUMENT
        XmlPullParser.START_TAG -> EventType.START_TAG
        XmlPullParser.END_TAG -> EventType.END_TAG
        XmlPullParser.TEXT -> EventType.TEXT
        XmlPullParser.CDSECT -> EventType.CDSECT
        XmlPullParser.ENTITY_REF  -> EventType.ENTITY_REF
        XmlPullParser.IGNORABLE_WHITESPACE -> EventType.IGNORABLE_WHITESPACE
        XmlPullParser.PROCESSING_INSTRUCTION -> EventType.PROCESSING_INSTRUCTION
        XmlPullParser.COMMENT -> EventType.COMMENT
        XmlPullParser.DOCDECL -> EventType.DOCDECL
        else -> throw XmlPullParserException("Unexpected event type: $this", parser, null)
    }

    private fun EventType.int(): Int = when(this) {
        EventType.START_DOCUMENT -> XmlPullParser.START_DOCUMENT
        EventType.END_DOCUMENT -> XmlPullParser.END_DOCUMENT
        EventType.START_TAG -> XmlPullParser.START_TAG
        EventType.END_TAG -> XmlPullParser.END_TAG
        EventType.TEXT -> XmlPullParser.TEXT
        EventType.CDSECT -> XmlPullParser.CDSECT
        EventType.ENTITY_REF  -> XmlPullParser.ENTITY_REF
        EventType.IGNORABLE_WHITESPACE -> XmlPullParser.IGNORABLE_WHITESPACE
        EventType.PROCESSING_INSTRUCTION -> XmlPullParser.PROCESSING_INSTRUCTION
        EventType.COMMENT -> XmlPullParser.COMMENT
        EventType.DOCDECL -> XmlPullParser.DOCDECL
    }
}