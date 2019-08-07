package uk.co.baconi.pka.common.openldbws.responses

import uk.co.baconi.pka.common.openldbws.departures.Departures
import uk.co.baconi.pka.common.openldbws.services.CallingPoint
import uk.co.baconi.pka.common.openldbws.services.CallingPoints

abstract class BaseDeparturesWithDetailsResponseTest : BaseDeparturesResponseTest() {

    override fun expected(): Departures {

        val departures = super.expected()
        val destination = departures.destinations?.first()

        return departures.copy(
            destinations = listOf(
                destination!!.copy(
                    service = destination.service?.copy(
                        subsequentCallingPoints = listOf(
                            CallingPoints(
                                listOf(
                                    CallingPoint(
                                        locationName = "Doncaster",
                                        crs = "DON",
                                        scheduledTime = "15:01",
                                        estimatedTime = "On time"
                                    ),
                                    CallingPoint(
                                        locationName = "Scunthorpe",
                                        crs = "SCU",
                                        scheduledTime = "15:32",
                                        estimatedTime = "On time"
                                    ),
                                    CallingPoint(
                                        locationName = "Barnetby",
                                        crs = "BTB",
                                        scheduledTime = "15:47",
                                        estimatedTime = "On time"
                                    ),
                                    CallingPoint(
                                        locationName = "Grimsby Town",
                                        crs = "GMB",
                                        scheduledTime = "16:07",
                                        estimatedTime = "On time"
                                    ),
                                    CallingPoint(
                                        locationName = "Cleethorpes",
                                        crs = "CLE",
                                        scheduledTime = "16:16",
                                        estimatedTime = "On time"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }
}