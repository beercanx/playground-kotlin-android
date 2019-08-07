package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.openldbws.services.CoachData.Companion.coaches
import uk.co.baconi.pka.common.xml.*

data class FormationData(
    val averageLoading: Int? = null,
    val coaches: List<CoachData>? = null
) {

    companion object {

        fun XmlDeserializer.formationData(): FormationData = parse("formation") { result ->
            when (getName()) {
                "avgLoading" -> result.copy(averageLoading = readAsInt())
                "coaches" -> result.copy(coaches = coaches())
                else -> skip(result)
            }
        }
    }
}