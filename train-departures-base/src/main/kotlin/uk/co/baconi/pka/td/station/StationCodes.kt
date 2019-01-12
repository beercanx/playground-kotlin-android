package uk.co.baconi.pka.td.station

/**
 * Source: http://www.nationalrail.co.uk/static/documents/content/station_codes.csv
 */
data class StationCode(val stationName: String, val crsCode: String)

object StationCodes {

    fun stationCodes(): List<StationCode> = readFromResource("station_codes.csv")

    /**
     * Returns predicate to filter by Station Name
     */
    fun byName(name: String): (StationCode) -> Boolean = { sc -> sc.stationName.contains(name.toUpperCase()) }

    /**
     * Returns predicate to filter by CRS Code
     */
    fun byCode(code: String): (StationCode) -> Boolean = { sc -> sc.crsCode.contains(code.toUpperCase()) }

    private fun readFromResource(resource: String, ignoreFirstLine: Boolean = true): List<StationCode> = javaClass
        .classLoader
        .getResourceAsStream(resource)
        .bufferedReader()
        .useLines { lines ->
            (if(ignoreFirstLine) lines.tail() else lines)
                .map(String::toUpperCase)
                .map { line -> line.split(',', limit = 2) }
                .filter { line -> line.size == 2 }
                .map { line -> StationCode(line[0], line[1]) }
                .toList()
        }

    private fun <T> Sequence<T>.tail() = drop(1)
}