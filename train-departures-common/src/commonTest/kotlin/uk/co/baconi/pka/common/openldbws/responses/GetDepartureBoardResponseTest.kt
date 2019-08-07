package uk.co.baconi.pka.common.openldbws.responses

import uk.co.baconi.pka.common.openldbws.departures.DepartureBoard
import uk.co.baconi.pka.common.openldbws.departures.DepartureBoard.Companion.departureBoard
import uk.co.baconi.pka.common.openldbws.requests.DepartureBoardType
import uk.co.baconi.pka.common.openldbws.requests.DepartureBoardType.DepartureBoardWithDetails
import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.openldbws.services.ServiceLocation
import uk.co.baconi.pka.common.soap.body
import uk.co.baconi.pka.common.soap.envelope
import uk.co.baconi.pka.common.soap.tag
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

open class GetDepartureBoardResponseTest {

    open val type: DepartureBoardType = DepartureBoardType.DepartureBoard

    @Test
    fun `Should be able to deserialise example response of Sheffield to Meadowhall`() {
        expect(expected()) {
            XmlDeserializer(sheffieldToMeadowhall).envelope {
                body(type.responseTag) {
                    tag(type.responseTag, "GetStationBoardResult") {
                        departureBoard()
                    }
                }
            }
        }
    }

    open fun expected(): DepartureBoard = DepartureBoard(
        generatedAt = "2019-01-13T13:51:17.106902+00:00",
        locationName = "Sheffield",
        crs = "SHF",
        filterLocationName = "Meadowhall",
        filterCrs = "MHS",
        platformAvailable = true,
        nrccMessages = listOf(
            "Disruption at Doncaster."
        ),
        trainServices = listOf(
            Service(
                scheduledDepartureTime = "17:11",
                estimatedDepartureTime = "Delayed",
                operator = "TransPennine Express",
                operatorCode = "TP",
                serviceType = "train",
                serviceID = "KNc2881g1nXc3mgpn3Ba1w==",
                retailServiceId = "TP604400",
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

    private val sheffieldToMeadowhall: String
        get() = ("""
            |<?xml version="1.0" encoding="utf-8"?>
            |<soap:Envelope
            |  xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
            |  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            |  xmlns:xsd="http://www.w3.org/2001/XMLSchema">
            |  <soap:Body>
            |    <${type.responseTag}
            |      xmlns="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
            |      <GetStationBoardResult
            |        xmlns:lt="http://thalesgroup.com/RTTI/2012-01-13/ldb/types"
            |        xmlns:lt6="http://thalesgroup.com/RTTI/2017-02-02/ldb/types"
            |        xmlns:lt7="http://thalesgroup.com/RTTI/2017-10-01/ldb/types"
            |        xmlns:lt4="http://thalesgroup.com/RTTI/2015-11-27/ldb/types"
            |        xmlns:lt5="http://thalesgroup.com/RTTI/2016-02-16/ldb/types"
            |        xmlns:lt2="http://thalesgroup.com/RTTI/2014-02-20/ldb/types"
            |        xmlns:lt3="http://thalesgroup.com/RTTI/2015-05-14/ldb/types">
            |        <lt4:generatedAt>2019-01-13T13:51:17.106902+00:00</lt4:generatedAt>
            |        <lt4:locationName>Sheffield</lt4:locationName>
            |        <lt4:crs>SHF</lt4:crs>
            |        <lt4:filterLocationName>Meadowhall</lt4:filterLocationName>
            |        <lt4:filtercrs>MHS</lt4:filtercrs>
            |        <lt4:nrccMessages>
            |          <lt:message>Disruption at Doncaster.</lt:message>
            |        </lt4:nrccMessages>
            |        <lt4:platformAvailable>true</lt4:platformAvailable>
            |        <lt7:trainServices>
            |          <lt7:service>
            |            <lt4:std>17:11</lt4:std>
            |            <lt4:etd>Delayed</lt4:etd>
            |            <lt4:operator>TransPennine Express</lt4:operator>
            |            <lt4:operatorCode>TP</lt4:operatorCode>
            |            <lt4:serviceType>train</lt4:serviceType>
            |            <lt4:serviceID>KNc2881g1nXc3mgpn3Ba1w==</lt4:serviceID>
            |            <lt5:rsid>TP604400</lt5:rsid>
            |            <lt5:origin>
            |              <lt4:location>
            |                <lt4:locationName>Manchester Airport</lt4:locationName>
            |                <lt4:crs>MIA</lt4:crs>
            |              </lt4:location>
            |            </lt5:origin>
            |            <lt5:destination>
            |              <lt4:location>
            |                <lt4:locationName>Cleethorpes</lt4:locationName>
            |                <lt4:crs>CLE</lt4:crs>
            |              </lt4:location>
            |            </lt5:destination>""" + if(type == DepartureBoardWithDetails) {
         """|            <lt7:subsequentCallingPoints>
            |               <lt7:callingPointList>
            |                  <lt7:callingPoint>
            |                    <lt7:locationName>Meadowhall</lt7:locationName>
            |                    <lt7:crs>MHS</lt7:crs>
            |                    <lt7:st>17:16</lt7:st>
            |                    <lt7:et>Delayed</lt7:et>
            |                  </lt7:callingPoint>
            |              </lt7:callingPointList>
            |            </lt7:subsequentCallingPoints> """ } else { "" } +
         """|          </lt7:service>
            |        </lt7:trainServices>
            |      </GetStationBoardResult>
            |    </${type.responseTag}>
            |  </soap:Body>
            |</soap:Envelope>
        """).trimMargin()
}