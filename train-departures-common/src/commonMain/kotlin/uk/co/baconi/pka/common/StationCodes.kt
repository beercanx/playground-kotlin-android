package uk.co.baconi.pka.common

internal expect fun loadStationCodes(): List<StationCode>

object StationCodes {

    val stationCodes: List<StationCode>
        get() = loadStationCodes().sorted()

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

}