package uk.co.baconi.pka.common.openldbws.requests

enum class DetailsType : RequestType {
    ServiceDetails {
        override val action = "http://thalesgroup.com/RTTI/2012-01-13/ldb/GetServiceDetails"
    }
}