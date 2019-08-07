package uk.co.baconi.pka.common

expect fun <T> runTest(block: suspend () -> T)