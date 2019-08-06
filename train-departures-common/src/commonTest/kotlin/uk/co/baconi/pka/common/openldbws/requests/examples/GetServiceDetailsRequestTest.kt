package uk.co.baconi.pka.common.openldbws.requests.examples

import uk.co.baconi.pka.common.openldbws.details.ServiceDetails
import uk.co.baconi.pka.common.openldbws.details.ServiceDetails.Companion.serviceDetails
import uk.co.baconi.pka.common.openldbws.requests.DetailsType
import uk.co.baconi.pka.common.openldbws.services.CallingPoint
import uk.co.baconi.pka.common.openldbws.services.CallingPoints
import uk.co.baconi.pka.common.soap.body
import uk.co.baconi.pka.common.soap.envelope
import uk.co.baconi.pka.common.soap.tag
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class GetServiceDetailsRequestTest {

    private val type = DetailsType.ServiceDetails

    @Test
    fun `Should be able to deserialise example response of service details`() {

        val expected = ServiceDetails(
            generatedAt = "2019-04-24T12:38:43.6915623+01:00",
            serviceType = "train",
            locationName = "Meadowhall",
            crs = "MHS",
            operator = "Northern",
            operatorCode = "NT",
            length = 2,
            platform = "1",
            scheduledArrivalTime = "12:34",
            actualArrivalTime = "12:36",
            scheduledDepartureTime = "12:34",
            actualDepartureTime = "12:37",
            previousCallingPoints = listOf(
                CallingPoints(
                    callingPoint = listOf(
                        CallingPoint(
                            locationName = "Hull",
                            crs = "HUL",
                            scheduledTime = "10:53",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Hessle",
                            crs = "HES",
                            scheduledTime = "11:00",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Ferriby",
                            crs = "FRY",
                            scheduledTime = "11:05",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Brough",
                            crs = "BUH",
                            scheduledTime = "11:10",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Gilberdyke",
                            crs = "GBD",
                            scheduledTime = "11:17",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Saltmarshe",
                            crs = "SAM",
                            scheduledTime = "11:23",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Goole",
                            crs = "GOO",
                            scheduledTime = "11:28",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Thorne North",
                            crs = "TNN",
                            scheduledTime = "11:37",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Hatfield & Stainforth",
                            crs = "HFS",
                            scheduledTime = "11:43",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Kirk Sandall",
                            crs = "KKS",
                            scheduledTime = "11:47",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Doncaster",
                            crs = "DON",
                            scheduledTime = "12:03",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Conisbrough",
                            crs = "CNS",
                            scheduledTime = "12:10",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Mexborough",
                            crs = "MEX",
                            scheduledTime = "12:15",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Swinton (South Yorkshire)",
                            crs = "SWN",
                            scheduledTime = "12:18",
                            actualTime = "On time",
                            length = 2
                        ),
                        CallingPoint(
                            locationName = "Rotherham Central",
                            crs = "RMC",
                            scheduledTime = "12:28",
                            actualTime = "12:32",
                            length = 2
                        )
                    )
                )
            ),
            subsequentCallingPoints = listOf(
                CallingPoints(
                    callingPoint = listOf(
                        CallingPoint(
                            locationName = "Sheffield",
                            crs = "SHF",
                            scheduledTime = "12:44",
                            estimatedTime = "On time",
                            length = 2
                        )
                    )
                )
            )
        )

        expect(expected) {
            XmlDeserializer(MEADOWHALL_TO_SHEFFIELD_SERVICE_DETAILS).envelope {
                body(type.responseTag) {
                    tag(type.responseTag, "GetServiceDetailsResult") {
                        serviceDetails()
                    }
                }
            }
        }
    }

    companion object {
        private val MEADOWHALL_TO_SHEFFIELD_SERVICE_DETAILS = """
            |<?xml version="1.0" encoding="utf-8"?>
            |<soap:Envelope
            |  xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
            |  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            |  xmlns:xsd="http://www.w3.org/2001/XMLSchema">
            |  <soap:Body>
            |    <GetServiceDetailsResponse
            |      xmlns="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
            |      <GetServiceDetailsResult
            |        xmlns:lt="http://thalesgroup.com/RTTI/2012-01-13/ldb/types"
            |        xmlns:lt6="http://thalesgroup.com/RTTI/2017-02-02/ldb/types"
            |        xmlns:lt7="http://thalesgroup.com/RTTI/2017-10-01/ldb/types"
            |        xmlns:lt4="http://thalesgroup.com/RTTI/2015-11-27/ldb/types"
            |        xmlns:lt5="http://thalesgroup.com/RTTI/2016-02-16/ldb/types"
            |        xmlns:lt2="http://thalesgroup.com/RTTI/2014-02-20/ldb/types"
            |        xmlns:lt3="http://thalesgroup.com/RTTI/2015-05-14/ldb/types">
            |        <lt7:generatedAt>2019-04-24T12:38:43.6915623+01:00</lt7:generatedAt>
            |        <lt7:serviceType>train</lt7:serviceType>
            |        <lt7:locationName>Meadowhall</lt7:locationName>
            |        <lt7:crs>MHS</lt7:crs>
            |        <lt7:operator>Northern</lt7:operator>
            |        <lt7:operatorCode>NT</lt7:operatorCode>
            |        <lt7:length>2</lt7:length>
            |        <lt7:platform>1</lt7:platform>
            |        <lt7:sta>12:34</lt7:sta>
            |        <lt7:ata>12:36</lt7:ata>
            |        <lt7:std>12:34</lt7:std>
            |        <lt7:atd>12:37</lt7:atd>
            |        <lt7:previousCallingPoints>
            |          <lt7:callingPointList>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Hull</lt7:locationName>
            |              <lt7:crs>HUL</lt7:crs>
            |              <lt7:st>10:53</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Hessle</lt7:locationName>
            |              <lt7:crs>HES</lt7:crs>
            |              <lt7:st>11:00</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Ferriby</lt7:locationName>
            |              <lt7:crs>FRY</lt7:crs>
            |              <lt7:st>11:05</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Brough</lt7:locationName>
            |              <lt7:crs>BUH</lt7:crs>
            |              <lt7:st>11:10</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Gilberdyke</lt7:locationName>
            |              <lt7:crs>GBD</lt7:crs>
            |              <lt7:st>11:17</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Saltmarshe</lt7:locationName>
            |              <lt7:crs>SAM</lt7:crs>
            |              <lt7:st>11:23</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Goole</lt7:locationName>
            |              <lt7:crs>GOO</lt7:crs>
            |              <lt7:st>11:28</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Thorne North</lt7:locationName>
            |              <lt7:crs>TNN</lt7:crs>
            |              <lt7:st>11:37</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Hatfield &amp; Stainforth</lt7:locationName>
            |              <lt7:crs>HFS</lt7:crs>
            |              <lt7:st>11:43</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Kirk Sandall</lt7:locationName>
            |              <lt7:crs>KKS</lt7:crs>
            |              <lt7:st>11:47</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Doncaster</lt7:locationName>
            |              <lt7:crs>DON</lt7:crs>
            |              <lt7:st>12:03</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Conisbrough</lt7:locationName>
            |              <lt7:crs>CNS</lt7:crs>
            |              <lt7:st>12:10</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Mexborough</lt7:locationName>
            |              <lt7:crs>MEX</lt7:crs>
            |              <lt7:st>12:15</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Swinton (South Yorkshire)</lt7:locationName>
            |              <lt7:crs>SWN</lt7:crs>
            |              <lt7:st>12:18</lt7:st>
            |              <lt7:at>On time</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Rotherham Central</lt7:locationName>
            |              <lt7:crs>RMC</lt7:crs>
            |              <lt7:st>12:28</lt7:st>
            |              <lt7:at>12:32</lt7:at>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |          </lt7:callingPointList>
            |        </lt7:previousCallingPoints>
            |        <lt7:subsequentCallingPoints>
            |          <lt7:callingPointList>
            |            <lt7:callingPoint>
            |              <lt7:locationName>Sheffield</lt7:locationName>
            |              <lt7:crs>SHF</lt7:crs>
            |              <lt7:st>12:44</lt7:st>
            |              <lt7:et>On time</lt7:et>
            |              <lt7:length>2</lt7:length>
            |            </lt7:callingPoint>
            |          </lt7:callingPointList>
            |        </lt7:subsequentCallingPoints>
            |      </GetServiceDetailsResult>
            |    </GetServiceDetailsResponse>
            |  </soap:Body>
            |</soap:Envelope>
        """.trimMargin()
    }
}