package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.openldbws.services.Toilet.Companion.toilet
import uk.co.baconi.pka.common.xml.*

data class CoachData(
    val coachClass: String? = null, // [First / Mixed / Standard]
    val loading: Int? = null, // The loading value in % (think this mean how full) [0-100]
    val number: String? = null, // Identifier [A / 12]
    val toilet: Toilet? = null
) {

    companion object {

        fun XmlDeserializer.coaches(): List<CoachData> = parse("coaches", emptyList()) { result ->
            when (getName()) {
                "coach" -> coach().let(result::plus)
                else -> skip(result)
            }
        }

        fun XmlDeserializer.coach(): CoachData = parse("coach") { result ->
            when (getName()) {
                "coachClass" -> result.copy(coachClass = readAsText())
                "loading" -> result.copy(loading = readAsInt())
                "number" -> result.copy(number = readAsText())
                "toilet" -> result.copy(toilet = toilet())
                else -> skip(result)
            }
        }
    }
}