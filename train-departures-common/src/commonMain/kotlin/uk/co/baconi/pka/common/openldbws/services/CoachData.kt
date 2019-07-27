package uk.co.baconi.pka.common.openldbws.services

data class CoachData(
    val coachClass: String? = null, // [First / Mixed / Standard]
    val loading: Int? = null, // The loading value in % (think this mean how full) [0-100]
    val number: String? = null, // Identifier [A / 12]
    val toilet: Toilet? = null
)