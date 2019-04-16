package uk.co.baconi.pka.tdb.openldbws.requests

import uk.co.baconi.pka.tdb.xml.attribute
import uk.co.baconi.pka.tdb.xml.build
import uk.co.baconi.pka.tdb.xml.tag
import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.StationCode
import uk.co.baconi.pka.tdb.xml.XmlParser

/**
 * Created by James Bacon on 12/01/2019.
 */
class GetNextDeparturesRequest(
    private val accessToken: AccessToken,
    private val from: StationCode,
    private val to: StationCode
) : Request {

    override val contentType = "text/xml;charset=UTF-8"

    override val action = "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDepartures"


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
     *          <ldb:GetNextDeparturesRequest>
     *              <ldb:crs>${from}</ldb:crs>
     *              <ldb:filterList>
     *                  <ldb:crs>${to}</ldb:crs>
     *              </ldb:filterList>
     *              <ldb:timeOffset>0</ldb:timeOffset>
     *              <ldb:timeWindow>120</ldb:timeWindow>
     *          </ldb:GetNextDeparturesRequest>
     *      </soap:Body>
     *  </soap:Envelope>
     */
    override val body: String = XmlParser.serializer().build {

        val soap = "soap"
        val types = "typ"
        val ldb = "ldb"

        tag("$soap:Envelope") {

            attribute("xmlns:$soap", "http://www.w3.org/2003/05/soap-envelope")
            attribute("xmlns:$types", "http://thalesgroup.com/RTTI/2013-11-28/Token/types")
            attribute("xmlns:$ldb", "http://thalesgroup.com/RTTI/2017-10-01/ldb/")

            tag("$soap:Header") {
                tag("$types:AccessToken") {
                    tag("$types:TokenValue") {
                        text(accessToken.value)
                    }
                }
            }
            tag("$soap:Body") {
                tag("$ldb:GetNextDeparturesRequest") {
                    tag("$ldb:crs") {
                        text(from.crsCode)
                    }
                    tag("$ldb:filterList") {
                        tag("$ldb:crs") {
                            text(to.crsCode)
                        }
                    }
                    tag("$ldb:timeOffset") {
                        text("0")
                    }
                    tag("$ldb:timeWindow") {
                        text("120")
                    }
                }
            }
        }
    }
}