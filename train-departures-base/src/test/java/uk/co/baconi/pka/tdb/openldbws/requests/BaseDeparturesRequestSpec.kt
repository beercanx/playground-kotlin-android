package uk.co.baconi.pka.tdb.openldbws.requests

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.StationCodes

class BaseDeparturesRequestSpec : StringSpec({

    val accessToken = AccessToken("test-token")
    val from = StationCodes.firstByCode("AVY")
    val to = StationCodes.firstByCode("ABH")

    mapOf(
        "GetNextDepartures" to GetNextDeparturesRequest(accessToken, from, to),
        "GetNextDeparturesWithDetails" to GetNextDeparturesWithDetailsRequest(accessToken, from, to),
        "GetFastestDepartures" to GetFastestDeparturesRequest(accessToken, from, to),
        "GetFastestDeparturesWithDetails" to GetFastestDeparturesWithDetailsRequest(accessToken, from, to)
    ).forEach { type, request ->

        "Should serialise into XML for type [$type]" {

            request.body shouldBe """
                <?xml version='1.0' encoding='utf-8' ?>
                <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:typ="http://thalesgroup.com/RTTI/2013-11-28/Token/types" xmlns:ldb="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
                    <soap:Header>
                        <typ:AccessToken>
                            <typ:TokenValue>${accessToken.value}</typ:TokenValue>
                        </typ:AccessToken>
                    </soap:Header>
                    <soap:Body>
                        <ldb:${type}Request>
                            <ldb:crs>${from.crsCode}</ldb:crs>
                            <ldb:filterList>
                                <ldb:crs>${to.crsCode}</ldb:crs>
                            </ldb:filterList>
                            <ldb:timeOffset>0</ldb:timeOffset>
                            <ldb:timeWindow>120</ldb:timeWindow>
                        </ldb:${type}Request>
                    </soap:Body>
                </soap:Envelope>
            """.lines().joinToString(separator = "", transform = String::trim)
        }
    }

})