package uk.co.baconi.pka.common.xml

import kotlinx.io.core.Closeable

expect class XmlDeserializer(input: String) : Closeable {
    fun isStartTag(): Boolean
    fun isEndTag(): Boolean
    fun isText(): Boolean
    fun requireStartTag(tag: String)
    fun requireEndTag(tag: String)
    fun getPrefix(): String
    fun getName(): String
    fun next(): Int
    fun nextTag(): Int
    fun getText(): String
    internal fun nextIs(expected: () -> Boolean): Boolean
}