package uk.co.baconi.pka.common.openldbws.services

/**
 * Covers both ServiceItem and ServiceItemWithCallingPoints (for parents of type WithDetails)
 */
data class Service(
    val scheduledArrivalTime: String? = null, // sta
    val estimatedArrivalTime: String? = null, // eta
    val scheduledDepartureTime: String? = null, // std
    val estimatedDepartureTime: String? = null, // etd
    val platform: String? = null,
    val operator: String? = null,
    val operatorCode: String? = null,
    val serviceType: String? = null,
    val serviceID: String? = null,
    val retailServiceId: String? = null, // rsid
    val origin: List<ServiceLocation>? = null,
    val destination: List<ServiceLocation>? = null,
    val currentOrigins: List<ServiceLocation>? = null,
    val currentDestinations: List<ServiceLocation>? = null,
    val isCircularRoute: Boolean? = null,
    val isCancelled: Boolean? = null,
    val filterLocationCancelled: Boolean? = null,
    val length: Int? = null,
    val detachFront: Boolean? = null,
    val isReverseFormation: Boolean? = null,
    val cancelReason: String? = null,
    val delayReason: String? = null,
    val adhocAlerts: List<String>? = null,
    val formation: FormationData? = null,
    val previousCallingPoints: List<CallingPoints>? = null,
    val subsequentCallingPoints: List<CallingPoints>? = null
)
