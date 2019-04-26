package uk.co.baconi.pka.tdb

/**
 * Source: http://www.nationalrail.co.uk/static/documents/content/station_codes.csv
 */
data class StationCode(val stationName: String, val crsCode: String)

object StationCodes {

    val stationCodes: List<StationCode>
        get() = readFromResource("station_codes.csv")

    fun firstByName(name: String) = stationCodes.first(byName(name))

    fun firstByCode(name: String) = stationCodes.first(byCode(name))

    /**
     * Returns predicate to filter by Station Name
     */
    fun byName(name: String): (StationCode) -> Boolean = { sc -> sc.stationName.toUpperCase().contains(name.toUpperCase()) }

    /**
     * Returns predicate to filter by CRS Code
     */
    fun byCode(code: String): (StationCode) -> Boolean = { sc -> sc.crsCode.toUpperCase().contains(code.toUpperCase()) }

    // TODO - Consider a better CSV format or better storage mechanism for minimalistic code for reading
    private fun readFromResource(resource: String, ignoreFirstLine: Boolean = true): List<StationCode> = javaClass
        .classLoader
        ?.getResourceAsStream(resource)
        ?.bufferedReader()
        ?.useLines { lines ->
            (if(ignoreFirstLine) lines.tail() else lines)
                .map { line -> line.split(',', limit = 2) }
                .filter { line -> line.size == 2 }
                .map { line -> StationCode(line[0], line[1]) }
                .toList()
        }
        ?: emptyList()

    private fun <T> Sequence<T>.tail() = drop(1)
}