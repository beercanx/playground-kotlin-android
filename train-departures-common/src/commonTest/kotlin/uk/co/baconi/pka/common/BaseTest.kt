package uk.co.baconi.pka.common

import kotlin.test.expect

interface BaseTest {

    infix fun <A> A.shouldBe(any: A?) {
        expect(any) {
            this
        }
    }

   fun String.trimFlat() = trimIndent().lines().joinToString("", transform = String::trim)

}