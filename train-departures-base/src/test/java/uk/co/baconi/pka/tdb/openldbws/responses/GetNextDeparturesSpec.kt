package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.assertSoftly
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe
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

        assertSoftly {

            val departuresBoard = result?.body?.getNextDeparturesResponse?.departuresBoard
            departuresBoard?.generatedAt shouldBe "2019-01-13T13:51:17.106902+00:00"
            departuresBoard?.locationName shouldBe "Sheffield"
            departuresBoard?.crs shouldBe "SHF"
            departuresBoard?.platformAvailable shouldBe true

            departuresBoard?.nrccMessages?.messages?.first() shouldContain("Disruption between Bristol Temple Meads and Taunton via Weston-super-Mare")

            val destinations = departuresBoard?.departures?.destination
            destinations?.size shouldBe 1
            destinations?.first()?.crs shouldBe "CLE"

            val service = destinations?.first()?.service
            service?.sta shouldBe "14:08"
            service?.eta shouldBe "On time"
            service?.std shouldBe "14:29"
            service?.etd shouldBe "On time"
            service?.platform shouldBe "1B"
            service?.operator shouldBe "TransPennine Express"
            service?.operatorCode shouldBe "TP"
            service?.serviceType shouldBe "train"
            service?.serviceID shouldBe "8ipq5Cv9fDMbR0rDg6riKA=="
            service?.rsid shouldBe "TP603200"
            service?.origin?.locationName shouldBe "Manchester Airport"
            service?.origin?.crs shouldBe "MIA"
            service?.destination?.locationName shouldBe "Cleethorpes"
            service?.destination?.crs shouldBe "CLE"
        }
    }
})