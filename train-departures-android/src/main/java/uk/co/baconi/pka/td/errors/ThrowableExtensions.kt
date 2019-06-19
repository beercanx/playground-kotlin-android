package uk.co.baconi.pka.td.errors

import java.io.PrintWriter
import java.io.StringWriter

fun Throwable.printStackTraceAsString() : String {
    val writer = StringWriter()
    printStackTrace(PrintWriter(writer))
    return writer.toString()
}

fun Throwable.toErrorParcel() : ErrorParcel {
    return ErrorParcel(
        this::class.toString(),
        this.message,
        this.printStackTraceAsString()
    )
}