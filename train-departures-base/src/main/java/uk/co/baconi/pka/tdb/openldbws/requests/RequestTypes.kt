package uk.co.baconi.pka.tdb.openldbws.requests

enum class RequestTypes(val action: String) {

    // Boards
    GetDepartureBoard("http://thalesgroup.com/RTTI/2012-01-13/ldb/GetDepartureBoard"),
    GetDepBoardWithDetails("http://thalesgroup.com/RTTI/2015-05-14/ldb/GetDepBoardWithDetails"),

    // Singles
    GetNextDepartures("http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDepartures"),
    GetNextDeparturesWithDetails("http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDeparturesWithDetails"),
    GetFastestDepartures("http://thalesgroup.com/RTTI/2015-05-14/ldb/GetFastestDepartures"),
    GetFastestDeparturesWithDetails("http://thalesgroup.com/RTTI/2015-05-14/ldb/GetFastestDeparturesWithDetails"),
}