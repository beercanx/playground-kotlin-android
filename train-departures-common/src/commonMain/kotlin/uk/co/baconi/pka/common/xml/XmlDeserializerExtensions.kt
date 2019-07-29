package uk.co.baconi.pka.common.xml

import uk.co.baconi.pka.common.reflect.createInstance

inline fun <reified A : Any> XmlDeserializer.parse(
    tag: String,
    noinline attributes: (A) -> A = { a -> a },
    noinline body: (A) -> A
): A {
    return parse(tag, A::class.createInstance(), attributes) { result, _ ->
        body(result)
    }
}

fun <A> XmlDeserializer.parse(
    tag: String,
    initial: A,
    attributes: (A) -> A = { a -> a },
    body: (A) -> A
): A {
    return parse(tag, initial, attributes) { result, _ ->
        body(result)
    }
}

inline fun <reified A : Any> XmlDeserializer.parse(
    tag: String,
    noinline parsingAttributes: (A) -> A = { a -> a },
    noinline parsingBody: (A, String?) -> A
): A {
    return parse(tag, A::class.createInstance(), parsingAttributes, parsingBody)
}

fun <A> XmlDeserializer.parse(
    tag: String,
    initial: A,
    attributes: (A) -> A = { a -> a },
    body: (A, String?) -> A
): A {

    require(EventType.START_TAG, tag)

    var result = initial
    val currentPrefix = getPrefix()

    result = attributes(result)

    while (next() != EventType.END_TAG) {
        if (getEventType() != EventType.START_TAG) {
            continue
        }
        result = body(result, currentPrefix)
    }

    require(EventType.END_TAG, tag)

    return result
}

fun <A> XmlDeserializer.skip(result: A): A {
    skip()
    return result
}

fun XmlDeserializer.skip() {
    if (getEventType() != EventType.START_TAG) {
        throw IllegalStateException()
    }
    var depth = 1
    loop@ while (depth != 0) {
        when (next()) {
            EventType.END_TAG -> depth--
            EventType.START_TAG -> depth++
            else -> continue@loop
        }
    }
}

fun XmlDeserializer.readAsText(): String? {
    var result: String? = null
    if (next() == EventType.TEXT) {
        result = getText()
        nextTag()
    }
    return result
}

fun XmlDeserializer.readAsBoolean(): Boolean? {
    return readAsText()?.toBoolean()
}

fun XmlDeserializer.readAsInt(): Int? {
    return readAsText()?.toIntOrNull()
}