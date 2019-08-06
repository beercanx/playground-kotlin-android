package uk.co.baconi.pka.common.openldbws.requests.examples

import uk.co.baconi.pka.common.openldbws.departures.Departures
import uk.co.baconi.pka.common.openldbws.departures.Departures.Companion.departures
import uk.co.baconi.pka.common.openldbws.departures.Destination
import uk.co.baconi.pka.common.openldbws.requests.DeparturesType
import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.openldbws.services.ServiceLocation
import uk.co.baconi.pka.common.soap.body
import uk.co.baconi.pka.common.soap.envelope
import uk.co.baconi.pka.common.soap.tag
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class GetNextDeparturesRequestTest {

    private val type = DeparturesType.NextDepartures

    @Test
    fun `Should be able to deserialise example response of Sheffield to Cleethorpes`() {

        val expected = Departures(
            generatedAt = "2019-01-13T13:51:17.106902+00:00",
            locationName = "Sheffield",
            crs = "SHF",
            platformAvailable = true,
            nrccMessages = listOf(
                "Disruption between Bristol Temple Meads and Taunton via Weston-super-Mare."
            ),
            destinations = listOf(
                Destination(
                    crs = "CLE",
                    service = Service(
                        scheduledArrivalTime = "14:08",
                        estimatedArrivalTime = "On time",
                        scheduledDepartureTime = "14:29",
                        estimatedDepartureTime = "On time",
                        platform = "1B",
                        operator = "TransPennine Express",
                        operatorCode = "TP",
                        serviceType = "train",
                        serviceID = "8ipq5Cv9fDMbR0rDg6riKA==",
                        retailServiceId = "TP603200",
                        origin = listOf(
                            ServiceLocation(
                                locationName = "Manchester Airport",
                                crs = "MIA"
                            )
                        ),
                        destination = listOf(
                            ServiceLocation(
                                locationName = "Cleethorpes",
                                crs = "CLE"
                            )
                        )
                    )
                )
            )
        )

        expect(expected) {
            XmlDeserializer(SHEFFIELD_TO_CLEETHORPES_NEXT_DEPARTURES).envelope {
                body(type.responseTag) {
                    tag(type.responseTag, "DeparturesBoard") {
                        departures()
                    }
                }
            }
        }
    }

    companion object {
        private val SHEFFIELD_TO_CLEETHORPES_NEXT_DEPARTURES = """
                |<?xml version="1.0" encoding="utf-8"?>
                |<soap:Envelope
                |    xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
                |    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                |    xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                |  <soap:Body>
                |    <GetNextDeparturesResponse
                |        xmlns="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
                |      <DeparturesBoard
                |          xmlns:lt="http://thalesgroup.com/RTTI/2012-01-13/ldb/types"
                |          xmlns:lt6="http://thalesgroup.com/RTTI/2017-02-02/ldb/types"
                |          xmlns:lt7="http://thalesgroup.com/RTTI/2017-10-01/ldb/types"
                |          xmlns:lt4="http://thalesgroup.com/RTTI/2015-11-27/ldb/types"
                |          xmlns:lt5="http://thalesgroup.com/RTTI/2016-02-16/ldb/types"
                |          xmlns:lt2="http://thalesgroup.com/RTTI/2014-02-20/ldb/types"
                |          xmlns:lt3="http://thalesgroup.com/RTTI/2015-05-14/ldb/types">
                |        <lt4:generatedAt>2019-01-13T13:51:17.106902+00:00</lt4:generatedAt>
                |        <lt4:locationName>Sheffield</lt4:locationName>
                |        <lt4:crs>SHF</lt4:crs>
                |        <lt4:nrccMessages>
                |          <lt:message>Disruption between Bristol Temple Meads and Taunton via Weston-super-Mare.</lt:message>
                |        </lt4:nrccMessages>
                |        <lt4:platformAvailable>true</lt4:platformAvailable>
                |        <lt7:departures>
                |          <lt7:destination crs="CLE">
                |            <lt7:service>
                |              <lt4:sta>14:08</lt4:sta>
                |              <lt4:eta>On time</lt4:eta>
                |              <lt4:std>14:29</lt4:std>
                |              <lt4:etd>On time</lt4:etd>
                |              <lt4:platform>1B</lt4:platform>
                |              <lt4:operator>TransPennine Express</lt4:operator>
                |              <lt4:operatorCode>TP</lt4:operatorCode>
                |              <lt4:serviceType>train</lt4:serviceType>
                |              <lt4:serviceID>8ipq5Cv9fDMbR0rDg6riKA==</lt4:serviceID>
                |              <lt5:rsid>TP603200</lt5:rsid>
                |              <lt5:origin>
                |                <lt4:location>
                |                  <lt4:locationName>Manchester Airport</lt4:locationName>
                |                  <lt4:crs>MIA</lt4:crs>
                |                </lt4:location>
                |              </lt5:origin>
                |              <lt5:destination>
                |                <lt4:location>
                |                  <lt4:locationName>Cleethorpes</lt4:locationName>
                |                  <lt4:crs>CLE</lt4:crs>
                |                </lt4:location>
                |              </lt5:destination>
                |            </lt7:service>
                |          </lt7:destination>
                |        </lt7:departures>
                |      </DeparturesBoard>
                |    </GetNextDeparturesResponse>
                |  </soap:Body>
                |</soap:Envelope>
                """.trimMargin()
    }

}