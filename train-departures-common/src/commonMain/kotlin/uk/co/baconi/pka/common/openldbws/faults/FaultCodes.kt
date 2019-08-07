package uk.co.baconi.pka.common.openldbws.faults

enum class FaultCodes {
    VersionMismatch,
    MustUnderstand,
    Receiver,
    Client,
    Server;

    companion object {
        fun lookup(value: String): FaultCodes? = values().find { type ->
            type.name.equals(value, ignoreCase = true)
        }
    }
}