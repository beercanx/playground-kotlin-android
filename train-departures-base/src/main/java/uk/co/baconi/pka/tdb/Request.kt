package uk.co.baconi.pka.tdb

interface Request {
    val headers: Map<String, String>
    val body: String
}