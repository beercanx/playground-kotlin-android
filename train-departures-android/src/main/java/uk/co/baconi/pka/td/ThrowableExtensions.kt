package uk.co.baconi.pka.td

import java.io.PrintWriter
import java.io.StringWriter

fun Throwable.printStackTraceAsString() : String {
    val writer = StringWriter()
    printStackTrace(PrintWriter(writer))
    return writer.toString()
}