package uk.co.baconi.pka.common.openldbws.services

data class ServiceLocation(
    val locationName: String? = null,
    val crs: String? = null,
    val via: String? = null,
    val futureChangeTo: String? = null,
    val associationIsCancelled: Boolean? = null
)