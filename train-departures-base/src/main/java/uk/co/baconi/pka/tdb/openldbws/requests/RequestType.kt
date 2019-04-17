package uk.co.baconi.pka.tdb.openldbws.requests

interface RequestType {
    val action: String
}

enum class DetailsRequestType(override val action: String) : RequestType {
    GetServiceDetails("http://thalesgroup.com/RTTI/2012-01-13/ldb/GetServiceDetails")
}

enum class DepartureBoardRequestType(override val action: String) : RequestType {
    GetDepartureBoard("http://thalesgroup.com/RTTI/2012-01-13/ldb/GetDepartureBoard"),
    GetDepBoardWithDetails("http://thalesgroup.com/RTTI/2015-05-14/ldb/GetDepBoardWithDetails")
}

enum class DeparturesRequestType(override val action: String) : RequestType {
    GetNextDepartures("http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDepartures"),
    GetNextDeparturesWithDetails("http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDeparturesWithDetails"),
    GetFastestDepartures("http://thalesgroup.com/RTTI/2015-05-14/ldb/GetFastestDepartures"),
    GetFastestDeparturesWithDetails("http://thalesgroup.com/RTTI/2015-05-14/ldb/GetFastestDeparturesWithDetails"),
}