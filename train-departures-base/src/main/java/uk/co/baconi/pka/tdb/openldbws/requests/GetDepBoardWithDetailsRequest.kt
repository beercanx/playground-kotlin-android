package uk.co.baconi.pka.tdb.openldbws.requests

import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.StationCode
import uk.co.baconi.pka.tdb.openldbws.requests.DepartureBoardRequestType.GetDepBoardWithDetails

class GetDepBoardWithDetailsRequest(
    accessToken: AccessToken,
    from: StationCode,
    to: StationCode
) : BaseDepartureBoardRequest(
    accessToken,
    from,
    to,
    GetDepBoardWithDetails
)
