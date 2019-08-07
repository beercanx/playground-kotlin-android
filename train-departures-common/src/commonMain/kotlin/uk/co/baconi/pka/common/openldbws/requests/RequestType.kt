package uk.co.baconi.pka.common.openldbws.requests

interface RequestType {
    val action: String
    val requestTag: String
    val responseTag: String
}