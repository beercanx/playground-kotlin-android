package uk.co.baconi.pka.td.stations

import android.widget.Filter
import uk.co.baconi.pka.common.stations.StationCode
import uk.co.baconi.pka.common.stations.StationCodes

class StationCodeFilter(private val adapter: StationCodeAdapter) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {

        val results: List<StationCode> = if(!constraint.isNullOrBlank()) {
            // TODO - Consider sorting to improve suggestions
            StationCodes.stationCodes.filter { stationCode ->
                stationCode.crsCode.contains(constraint, true) || stationCode.stationName.contains(constraint, true)
            }
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
}