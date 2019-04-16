package uk.co.baconi.pka.tdb.openldbws.requests

interface Request {

    val action: String
        get() = type.action

    val body: String

    val contentType: String
        get() = "text/xml;charset=UTF-8"

    val type: RequestTypes
}
