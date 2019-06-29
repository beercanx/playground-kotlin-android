package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class BaseDepartureBoardResponseSpec : StringSpec({

    listOf(
        "GetDepartureBoardResponse",
        "GetDepBoardWithDetailsResponse"
    ).forEach { type ->

        "Should decode with no response present [$type]" {

            BaseDepartureBoardResponse.fromXml(
                XmlParser.fromReader(
                    "<$type/>".reader()
                ),
                type
            ).stationBoardResult shouldBe null

            BaseDepartureBoardResponse.fromXml(
                XmlParser.fromReader(
                    "<$type></$type>".reader()
                ),
                type
            ).stationBoardResult shouldBe null
        }

        "Should decode with response present [$type]" {

            BaseDepartureBoardResponse.fromXml(
                XmlParser.fromReader(
                    "<$type><GetStationBoardResult/></$type>".reader()
                ),
                type
            ).stationBoardResult should beInstanceOf<StationBoardResult>()

            BaseDepartureBoardResponse.fromXml(
                XmlParser.fromReader(
                    "<$type><GetStationBoardResult></GetStationBoardResult></$type>".reader()
                ),
                type
            ).stationBoardResult should beInstanceOf<StationBoardResult>()
        }
    }
})