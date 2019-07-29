package uk.co.baconi.pka.common.xml

/**
 * Ripped from XmlPullParser
 */
enum class EventType {
    START_DOCUMENT,
    END_DOCUMENT,
    START_TAG,
    END_TAG,
    TEXT,
    CDSECT,
    ENTITY_REF,
    IGNORABLE_WHITESPACE,
    PROCESSING_INSTRUCTION,
    COMMENT,
    DOCDECL
}