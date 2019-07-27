package uk.co.baconi.pka.common.openldbws.requests

import uk.co.baconi.pka.common.AccessToken
import uk.co.baconi.pka.common.xml.XmlSerializer
import uk.co.baconi.pka.common.xml.build
import uk.co.baconi.pka.common.xml.soapRequest
import uk.co.baconi.pka.common.xml.tag

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
internal actual fun serviceDetailsRequest(accessToken: AccessToken, serviceID: String): String = XmlSerializer().build {
    soapRequest(accessToken) {
        tag("ldb:GetServiceDetailsRequest") {
            tag("ldb:serviceID") {
                text(serviceID)
            }
        }
    }
}