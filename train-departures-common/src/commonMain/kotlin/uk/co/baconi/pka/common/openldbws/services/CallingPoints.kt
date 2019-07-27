package uk.co.baconi.pka.common.openldbws.services

data class CallingPoints(
    val callingPoint: List<CallingPoint>? = null,
    val serviceType: String? = "train",
    val serviceChangeRequired: Boolean? = false,
    val associationIsCancelled: Boolean? = false
)