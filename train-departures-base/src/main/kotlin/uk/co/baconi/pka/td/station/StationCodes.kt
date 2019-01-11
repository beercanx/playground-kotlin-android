package uk.co.baconi.pka.td.station

/**
 * Source: http://www.nationalrail.co.uk/static/documents/content/station_codes.csv
 */
data class StationCode(val stationName: String, val crsCode: String)

object StationCodes {

    private val stationCodesCsv: Sequence<StationCode> = readFromResource("station_codes.csv")

    private fun readFromResource(resource: String): Sequence<StationCode> = this::class.java
        .getResourceAsStream(resource)
        .bufferedReader()
        .useLines { lines ->
            lines
                .map { line -> line.split(',', limit = 2) }
                .filter { line -> line.size == 2 }
                .map { line -> StationCode(line[0], line[1]) }
        }
}