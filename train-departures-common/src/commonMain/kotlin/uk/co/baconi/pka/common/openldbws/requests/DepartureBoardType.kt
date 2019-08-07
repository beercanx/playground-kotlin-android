package uk.co.baconi.pka.common.openldbws.requests

enum class DepartureBoardType : RequestType {
    DepartureBoard {
        override val requestTag = "GetDepartureBoardRequest"
        override val responseTag = "GetDepartureBoardResponse"
        override val action = "http://thalesgroup.com/RTTI/2012-01-13/ldb/GetDepartureBoard"
    },
    DepartureBoardWithDetails {
        override val requestTag = "GetDepBoardWithDetailsRequest"
        override val responseTag = "GetDepBoardWithDetailsResponse"
        override val action = "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetDepBoardWithDetails"
    };
}
