package uk.co.baconi.pka.tdb.openldbws.requests

import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.StationCode
import uk.co.baconi.pka.tdb.openldbws.requests.RequestTypes.GetNextDeparturesWithDetails

class GetNextDeparturesWithDetailsRequest(
    accessToken: AccessToken,
    from: StationCode,
    to: StationCode
) : BaseDeparturesRequest(
    accessToken,
    from,
    to,
    GetNextDeparturesWithDetails
)
