package uk.co.baconi.pka.common.xml

import kotlinx.io.core.Closeable

expect class XmlDeserializer(input: String) : Closeable {
    fun require(type: EventType, name: String, namespace: String? = null)
    fun getAttributeValue(name: String, namespace: String? = null): String?
    fun getPrefix(): String?
    fun getName(): String?
    fun next(): EventType
    fun nextTag(): EventType
    fun getText(): String?
    fun getEventType(): EventType
}