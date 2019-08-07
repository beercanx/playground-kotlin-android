package uk.co.baconi.pka.common

import kotlinx.coroutines.runBlocking
import java.io.File

actual fun <T> runTest(block: suspend () -> T) {

    File("abc").endsWith("")

    runBlocking {
        block()
    }
}