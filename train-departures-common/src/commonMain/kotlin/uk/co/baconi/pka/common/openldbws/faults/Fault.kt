package uk.co.baconi.pka.common.openldbws.faults

data class Fault(val code: FaultCodes? = null, val reason: String? = null) : Exception(
    "OpenLDBWS returned a fault of [$code] with reason [$reason]"
)