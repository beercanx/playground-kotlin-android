package uk.co.baconi.pka.tdb.openldbws.requests

import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.StationCode
import uk.co.baconi.pka.tdb.openldbws.requests.DeparturesRequestType.GetNextDepartures

class GetNextDeparturesRequest(
    accessToken: AccessToken,
    from: StationCode,
    to: StationCode
) : BaseDeparturesRequest(
    accessToken,
    from,
    to,
    GetNextDepartures
)
