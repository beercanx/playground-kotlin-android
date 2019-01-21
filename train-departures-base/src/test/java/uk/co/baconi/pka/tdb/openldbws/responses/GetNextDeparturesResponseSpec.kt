package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class GetNextDeparturesResponseSpec : StringSpec({

    "Should decode with no response present" {

        GetNextDeparturesResponse.fromXml(
            XmlParser.fromReader(
                "<GetNextDeparturesResponse/>".reader()
            )
        ).departuresBoard shouldBe null

        GetNextDeparturesResponse.fromXml(
            XmlParser.fromReader(
                "<GetNextDeparturesResponse></GetNextDeparturesResponse>".reader()
            )
        ).departuresBoard shouldBe null
    }

    "Should decode with response present" {

        GetNextDeparturesResponse.fromXml(
            XmlParser.fromReader(
                "<GetNextDeparturesResponse><DeparturesBoard/></GetNextDeparturesResponse>".reader()
            )
        ).departuresBoard should beInstanceOf<DeparturesBoard>()

        GetNextDeparturesResponse.fromXml(
            XmlParser.fromReader(
                "<GetNextDeparturesResponse><DeparturesBoard></DeparturesBoard></GetNextDeparturesResponse>".reader()
            )
        ).departuresBoard should beInstanceOf<DeparturesBoard>()
    }
})