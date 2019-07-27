package uk.co.baconi.pka.common.openldbws.requests

enum class DeparturesType : RequestType {
    NextDepartures {
        override val action = "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDepartures"
    },
    NextDeparturesWithDetails {
        override val action = "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDeparturesWithDetails"
    },
    FastestDepartures {
        override val action = "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetFastestDepartures"
    },
    FastestDeparturesWithDetails {
        override val action = "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetFastestDeparturesWithDetails"
    }
}
