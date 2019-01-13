package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec

class GetNextDeparturesSpec : StringSpec({

    "Should be able to deserialise example response of Sheffield to Cleethorpes" {

        val inputStream = GetNextDeparturesSpec::class.java
            .classLoader
            ?.getResourceAsStream("openldbws/GetNextDepartures_SHF_to_CLE.xml")

        inputStream shouldNotBe null

        val result: Envelope? = inputStream?.let { Envelope.fromInputStream(it) }

        result shouldNotBe null

        // TODO - Verify data in objects based on test resource
    }
})