package uk.co.baconi.pka.tdb.openldbws.requests

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.AccessToken
import java.util.UUID

class GetServiceDetailsRequestSpec : StringSpec({

    val accessToken = AccessToken("test-token")
    val serviceId = UUID.randomUUID().toString()
    val request = GetServiceDetailsRequest(accessToken, serviceId)

    "Should serialise into XML for GetServiceDetailsRequest" {

        request.body shouldBe """
                <?xml version='1.0' encoding='utf-8' ?>
                <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:typ="http://thalesgroup.com/RTTI/2013-11-28/Token/types" xmlns:ldb="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
                   <soap:Header>
                      <typ:AccessToken>
                         <typ:TokenValue>${accessToken.value}</typ:TokenValue>
                      </typ:AccessToken>
                   </soap:Header>
                   <soap:Body>
                      <ldb:GetServiceDetailsRequest>
                         <ldb:serviceID>$serviceId</ldb:serviceID>
                      </ldb:GetServiceDetailsRequest>
                   </soap:Body>
                </soap:Envelope>
            """.lines().joinToString(separator = "", transform = String::trim)
    }

})