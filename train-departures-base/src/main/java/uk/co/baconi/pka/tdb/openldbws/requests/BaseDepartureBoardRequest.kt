package uk.co.baconi.pka.tdb.openldbws.requests

import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.StationCode
import uk.co.baconi.pka.tdb.xml.*

abstract class BaseDepartureBoardRequest(
    private val accessToken: AccessToken,
    private val from: StationCode,
    private val to: StationCode,
    override val type: DepartureBoardRequestType
) : Request {

    /**
     *  Example XML request
     *
     *  <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
     *                 xmlns:typ="http://thalesgroup.com/RTTI/2013-11-28/Token/types"
     *                 xmlns:ldb="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
     *      <soap:Header>
     *          <typ:AccessToken>
     *              <typ:TokenValue>${accessToken}</typ:TokenValue>
     *          </typ:AccessToken>
     *      </soap:Header>
     *      <soap:Body>
     *          <ldb:${type}Request>
     *              <ldb:crs>${from}</ldb:crs>
     *              <ldb:filterCrs>${to}</ldb:filterCrs>
     *              <ldb:filterType>to</ldb:filterType>
     *              <ldb:numRows>5</ldb:numRows>
     *              <ldb:timeOffset>0</ldb:timeOffset>
     *              <ldb:timeWindow>120</ldb:timeWindow>
     *          </ldb:${type}Request>
     *      </soap:Body>
     *  </soap:Envelope>
     */
    override val body: String = XmlParser.serializer().build {
        soapRequest(accessToken) {
            tag("ldb:${type}Request") {
                tag("ldb:crs") {
                    text(from.crsCode)
                }
                tag("ldb:filterCrs") {
                    text(to.crsCode)
                }
                tag("ldb:filterType") {
                    text("to")
                }
                tag("ldb:numRows") {
                    text("5")
                }
                tag("ldb:timeOffset") {
                    text("0")
                }
                tag("ldb:timeWindow") {
                    text("120")
                }
            }
        }
    }
}