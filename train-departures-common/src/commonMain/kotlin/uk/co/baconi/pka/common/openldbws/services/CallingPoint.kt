package uk.co.baconi.pka.common.openldbws.services

data class CallingPoint(
    val locationName: String? = null,
    val crs: String? = null,
    val scheduledTime: String? = null,
    val estimatedTime: String? = null,
    val actualTime: String? = null,
    val isCancelled: Boolean? = null,
    val length: Int? = null,
    val detachFront: Boolean? = null,
    val formation: FormationData? = null,
    val adhocAlerts: List<String>? = null
)