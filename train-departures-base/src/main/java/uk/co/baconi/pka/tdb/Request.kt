package uk.co.baconi.pka.tdb

interface Request {
    val contentType: String
    val action: String
    val body: String
}