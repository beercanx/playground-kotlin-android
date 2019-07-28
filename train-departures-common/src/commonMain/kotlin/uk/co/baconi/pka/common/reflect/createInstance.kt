package uk.co.baconi.pka.common.reflect

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

fun <T : Any> KClass<T>.createInstance(): T {

    val noArgsConstructor = constructors.singleOrNull {
        it.parameters.all(KParameter::isOptional)
    } ?: throw IllegalArgumentException(
        "Class should have a single no-arg constructor: $this"
    )

    return noArgsConstructor.callBy(emptyMap())
}