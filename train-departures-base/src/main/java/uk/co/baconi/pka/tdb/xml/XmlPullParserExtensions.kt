package uk.co.baconi.pka.tdb.xml

import org.xmlpull.v1.XmlPullParser
import kotlin.reflect.full.createInstance

inline fun <reified A : Any> XmlPullParser.parse(
    tag: String,
    noinline parsingAttributes: (A) -> A = { a -> a },
    noinline parsingBody: (A) -> A
): A {
    return parse(tag, A::class.createInstance(), parsingAttributes) { result, _ ->
        parsingBody(result)
    }
}

fun <A> XmlPullParser.parse(
    tag: String,
    initial: A,
    parsingAttributes: (A) -> A = { a -> a },
    parsingBody: (A) -> A
): A {
    return parse(tag, initial, parsingAttributes) { result, _ ->
        parsingBody(result)
    }
}

inline fun <reified A : Any> XmlPullParser.parse(
    tag: String,
    noinline parsingAttributes: (A) -> A = { a -> a },
    noinline parsingBody: (A, String?) -> A
): A {
    return parse(tag, A::class.createInstance(), parsingAttributes, parsingBody)
}

fun <A> XmlPullParser.parse(
    tag: String,
    initial: A,
    parsingAttributes: (A) -> A = { a -> a },
    parsingBody: (A, String?) -> A
): A {

    require(XmlPullParser.START_TAG, null, tag)

    var result = initial
    val currentPrefix = prefix

    result = parsingAttributes(result)

    while (next() != XmlPullParser.END_TAG) {
        if (eventType != XmlPullParser.START_TAG) {
            continue
        }
        result = parsingBody(result, currentPrefix)
    }

    require(XmlPullParser.END_TAG, null, tag)

    return result
}

fun <A> XmlPullParser.skip(result: A): A {
    skip()
    return result
}

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