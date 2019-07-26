package uk.co.baconi.pka.common

// TODO - Consider a better CSV format or better storage mechanism for minimalistic code for reading
internal actual fun loadStationCodes(): List<StationCode> = StationCodes
    .javaClass
    .classLoader
    ?.getResourceAsStream("station_codes.csv")
    ?.bufferedReader()
    ?.useLines { lines ->
        lines
            .tail()
            .map { line -> line.split(',', limit = 2) }
            .filter { line -> line.size == 2 }
            .map { line -> StationCode(line[0], line[1]) }
            .toList()
    }
    ?: emptyList()

private fun <T> Sequence<T>.tail() = drop(1)
