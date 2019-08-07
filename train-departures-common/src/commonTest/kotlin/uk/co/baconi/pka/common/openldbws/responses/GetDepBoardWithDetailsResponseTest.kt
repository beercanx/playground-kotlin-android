package uk.co.baconi.pka.common.openldbws.responses

import uk.co.baconi.pka.common.openldbws.departures.DepartureBoard
import uk.co.baconi.pka.common.openldbws.requests.DepartureBoardType
import uk.co.baconi.pka.common.openldbws.requests.DepartureBoardType.DepartureBoardWithDetails
import uk.co.baconi.pka.common.openldbws.services.CallingPoint
import uk.co.baconi.pka.common.openldbws.services.CallingPoints

class GetDepBoardWithDetailsResponseTest : GetDepartureBoardResponseTest() {

    override val type: DepartureBoardType = DepartureBoardWithDetails

    override fun expected(): DepartureBoard = super.expected().run {
        copy(
            trainServices = listOf(
                trainServices!!.first().run {
                    copy(
                        subsequentCallingPoints = listOf(
                            CallingPoints(
                                listOf(
                                    CallingPoint(
                                        locationName = "Meadowhall",
                                        crs = "MHS",
                                        scheduledTime = "17:16",
                                        estimatedTime = "Delayed"
                                    )
                                )
                            )
                        )
                    )
                }
            )
        )
    }
}