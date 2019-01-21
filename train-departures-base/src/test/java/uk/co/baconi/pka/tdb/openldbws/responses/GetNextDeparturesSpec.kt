package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.assertSoftly
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.TestExtensions.toResourceInputStream

class GetNextDeparturesSpec : StringSpec({

    "Should be able to deserialise example response of Sheffield to Cleethorpes" {

        val result: Envelope = Envelope.fromInputStream(
            "openldbws/responses/GetNextDepartures_SHF_to_CLE.xml".toResourceInputStream()
        )

        assertSoftly {

            result.body should beInstanceOf<BodySuccess>()

            val body = result.body as BodySuccess

            val departuresBoard = body.getNextDeparturesResponse?.departuresBoard
            departuresBoard?.generatedAt shouldBe "2019-01-13T13:51:17.106902+00:00"
            departuresBoard?.locationName shouldBe "Sheffield"
            departuresBoard?.crs shouldBe "SHF"
            departuresBoard?.platformAvailable shouldBe true

            departuresBoard?.nrccMessages?.first() shouldContain("Disruption between Bristol Temple Meads and Taunton via Weston-super-Mare")

            val departureItems = departuresBoard?.departures?.departureItems
            departureItems?.size shouldBe 1

            val departureItem = departureItems?.first()
            departureItem?.crs shouldBe "CLE"

            val service = departureItem?.service
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

            val origins = service?.origins
            origins?.size shouldBe 1

            val origin = origins?.first()
            origin?.locationName shouldBe "Manchester Airport"
            origin?.crs shouldBe "MIA"

            val destinations = service?.destinations
            destinations?.size shouldBe 1

            val destination = destinations?.first()
            destination?.locationName shouldBe "Cleethorpes"
            destination?.crs shouldBe "CLE"
        }
    }
})