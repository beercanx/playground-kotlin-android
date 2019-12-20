package uk.co.baconi.pka.td.stations

import android.widget.Filter
import uk.co.baconi.pka.common.stations.StationCode
import uk.co.baconi.pka.common.stations.StationCodes
import  uk.co.baconi.pka.td.thenComparingWith

class StationCodeFilter(private val adapter: StationCodeAdapter) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {

        val results: List<StationCode> = if(!constraint.isNullOrBlank()) {
            StationCodes.stationCodes.filter { stationCode ->
                stationCode.crsCode.contains(constraint, true) || stationCode.stationName.contains(constraint, true)
            }.sortedWith(
                levenshteinDistanceFrom(constraint)
            )
        } else {
            emptyList()
        }

        return FilterResults().apply {
            values = results
            count = results.size
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapter.clear()
        adapter.addAll(results.values as List<StationCode>)
    }

    private fun levenshteinDistanceFrom(constraint: CharSequence): Comparator<StationCode> {
        val byCrsCode = LevenshteinConstraintComparator(constraint, StationCode::crsCode, ignoreCase = true)
        val byStationName = LevenshteinConstraintComparator(constraint, StationCode::stationName)
        return byCrsCode.thenComparingWith(byStationName)
    }
}