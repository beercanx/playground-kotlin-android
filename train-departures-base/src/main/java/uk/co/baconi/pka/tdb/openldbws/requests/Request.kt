package uk.co.baconi.pka.tdb.openldbws.requests

interface Request {
    val contentType: String
    val action: String
    val body: String
}