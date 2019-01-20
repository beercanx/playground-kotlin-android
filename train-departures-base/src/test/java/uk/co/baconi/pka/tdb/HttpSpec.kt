package uk.co.baconi.pka.tdb

import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.runBlocking
import java.lang.RuntimeException

class HttpSpec : StringSpec({

    val accessToken: AccessToken by lazy {
        AccessToken(
            System.getenv("ACCESS_TOKEN") ?: throw RuntimeException("Missing ACCESS_TOKEN environment variable")
        )
    }

    val from = StationCodes.firstByCode("MHS")
    val to = StationCodes.firstByCode("SHF")

    // Disabled as this was being used for local testing
    "[Integration Test] Call service and print out the results".config(enabled = false) {
        runBlocking {
            val result = Http.performGetNextDeparturesRequest(accessToken, from, to)

            result shouldNotBe null

            println("result: $result")
        }
    }
})