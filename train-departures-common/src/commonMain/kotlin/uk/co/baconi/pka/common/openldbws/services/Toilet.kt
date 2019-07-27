package uk.co.baconi.pka.common.openldbws.services

data class Toilet(
    val status: ToiletStatus? = null,
    val type: String? = null // [Unknown / None / Standard / Accessible]
)