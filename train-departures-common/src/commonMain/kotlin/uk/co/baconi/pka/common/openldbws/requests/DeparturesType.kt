package uk.co.baconi.pka.common.openldbws.requests

enum class DeparturesType : RequestType {
    NextDepartures {
        override val requestTag = "GetNextDeparturesRequest"
        override val responseTag = "GetNextDeparturesResponse"
        override val action = "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDepartures"
    },
    NextDeparturesWithDetails {
        override val requestTag = "GetNextDeparturesWithDetailsRequest"
        override val responseTag = "GetNextDeparturesWithDetailsResponse"
        override val action = "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDeparturesWithDetails"
    },
    FastestDepartures {
        override val requestTag = "GetFastestDeparturesRequest"
        override val responseTag = "GetFastestDeparturesResponse"
        override val action = "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetFastestDepartures"
    },
    FastestDeparturesWithDetails {
        override val requestTag = "GetFastestDeparturesWithDetailsRequest"
        override val responseTag = "GetFastestDeparturesWithDetailsResponse"
        override val action = "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetFastestDeparturesWithDetails"
    };
}
