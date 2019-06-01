package uk.co.baconi.pka.tdb.openldbws.requests

import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.xml.XmlParser
import uk.co.baconi.pka.tdb.xml.build
import uk.co.baconi.pka.tdb.xml.soapRequest
import uk.co.baconi.pka.tdb.xml.tag

class GetServiceDetailsRequest(
    private val accessToken: AccessToken,
    private val serviceID: String // Base64 encoded string
) : Request {

    override val type: RequestType = DetailsRequestType.GetServiceDetails

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
     *          <ldb:GetServiceDetailsRequest>
     *              <ldb:serviceID>${serviceID}</ldb:serviceID>
     *          </ldb:GetServiceDetailsRequest>
     *      </soap:Body>
     *  </soap:Envelope>
     */
    override val body: String = XmlParser.serializer().build {
        soapRequest(accessToken) {
            tag("ldb:GetServiceDetailsRequest") {
                tag("ldb:serviceID") {
                    text(serviceID)
                }
            }
        }
    }
}