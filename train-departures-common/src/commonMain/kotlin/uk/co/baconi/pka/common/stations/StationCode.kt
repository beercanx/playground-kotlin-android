package uk.co.baconi.pka.common.stations

/**
 * Source: http://www.nationalrail.co.uk/static/documents/content/station_codes.csv
 */
data class StationCode(val stationName: String, val crsCode: String) : Comparable<StationCode> {
    override fun compareTo(other: StationCode): Int  = stationName.compareTo(other.stationName)
}
