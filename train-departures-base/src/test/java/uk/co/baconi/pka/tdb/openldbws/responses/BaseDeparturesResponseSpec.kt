package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class BaseDeparturesResponseSpec : StringSpec({

    listOf(
        "GetNextDeparturesResponse",
        "GetNextDeparturesWithDetailsResponse",
        "GetFastestDeparturesResponse",
        "GetFastestDeparturesWithDetailsResponse"
    ).forEach { type ->

        "Should decode with no response present [$type]" {

            BaseDeparturesResponse.fromXml(
                XmlParser.fromReader(
                    "<$type/>".reader()
                ),
                type
            ).departuresBoard shouldBe null

            BaseDeparturesResponse.fromXml(
                XmlParser.fromReader(
                    "<$type></$type>".reader()
                ),
                type
            ).departuresBoard shouldBe null
        }

        "Should decode with response present [$type]" {

            BaseDeparturesResponse.fromXml(
                XmlParser.fromReader(
                    "<$type><DeparturesBoard/></$type>".reader()
                ),
                type
            ).departuresBoard should beInstanceOf<DeparturesBoard>()

            BaseDeparturesResponse.fromXml(
                XmlParser.fromReader(
                    "<$type><DeparturesBoard></DeparturesBoard></$type>".reader()
                ),
                type
            ).departuresBoard should beInstanceOf<DeparturesBoard>()
        }
    }
})