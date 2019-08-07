package uk.co.baconi.pka.common.openldbws.services

enum class ToiletStatus {
    Unknown,
    InService,
    NotInService;

    companion object {
        fun lookup(value: String): ToiletStatus? = values().find {type -> type.name == value }
    }
}