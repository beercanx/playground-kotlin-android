package uk.co.baconi.pka.common.openldbws.details

import uk.co.baconi.pka.common.openldbws.services.CallingPoints
import uk.co.baconi.pka.common.openldbws.services.FormationData

data class ServiceDetails(
    val generatedAt: String? = null,
    val serviceType: String? = null,
    val locationName: String? = null,
    val crs: String? = null,
    val operator: String? = null,
    val operatorCode: String? = null,
    val rsid: String? = null,
    val isCancelled: Boolean? = null,
    val cancelReason: String? = null,
    val delayReason: String? = null,
    val overdueMessage: String? = null,
    val length: Int? = null,
    val detachFront: Boolean? = null,
    val isReverseFormation: Boolean? = null,
    val platform: String? = null,
    val scheduledArrivalTime: String? = null, // sta
    val estimatedArrivalTime: String? = null, // eta
    val actualArrivalTime: String? = null, // ata
    val scheduledDepartureTime: String? = null, // std
    val estimatedDepartureTime: String? = null, // etd
    val actualDepartureTime: String? = null, // atd
    val adhocAlerts: List<String>? = null,
    val formation: FormationData? = null,
    val previousCallingPoints: List<CallingPoints>? = null,
    val subsequentCallingPoints: List<CallingPoints>? = null
)