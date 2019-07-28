package uk.co.baconi.pka.common.xml

import uk.co.baconi.pka.common.reflect.createInstance

inline fun <reified A : Any> XmlDeserializer.parse(
    tag: String,
    noinline parsingAttributes: (A) -> A = { a -> a },
    noinline parsingBody: (A) -> A
): A {
    return parse(tag, A::class.createInstance(), parsingAttributes) { result, _ ->
        parsingBody(result)
    }
}

fun <A> XmlDeserializer.parse(
    tag: String,
    initial: A,
    parsingAttributes: (A) -> A = { a -> a },
    parsingBody: (A) -> A
): A {
    return parse(tag, initial, parsingAttributes) { result, _ ->
        parsingBody(result)
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
    parsingAttributes: (A) -> A = { a -> a },
    parsingBody: (A, String?) -> A
): A {

    requireStartTag(tag)

    var result = initial
    val currentPrefix = getPrefix()

    result = parsingAttributes(result)

    while (!nextIs(::isEndTag)) {
        if (isStartTag()) {
            continue
        }
        result = parsingBody(result, currentPrefix)
    }

    requireEndTag(tag)

    return result
}

fun <A> XmlDeserializer.skip(result: A): A {
    skip()
    return result
}

fun XmlDeserializer.skip() {
    if (!isStartTag()) {
        throw IllegalStateException()
    }
    var depth = 1
    while (depth != 0) {
        next()
        when {
            isEndTag() -> depth--
            isStartTag() -> depth++
        }
    }
}

fun XmlDeserializer.readAsText(): String? {
    var result: String? = null
    if (nextIs(::isText)) {
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