package uk.co.baconi.pka.tdb

import io.kotlintest.shouldNotBe
import java.io.InputStream

object TestExtensions {

    fun String.toResourceInputStream(): InputStream = TestExtensions::class.java
        .classLoader
        ?.getResourceAsStream(this)
        .let { inputStream ->
            inputStream shouldNotBe null
            inputStream!!
        }
}