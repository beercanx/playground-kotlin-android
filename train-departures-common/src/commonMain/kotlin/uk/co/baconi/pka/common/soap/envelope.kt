package uk.co.baconi.pka.common.soap

import uk.co.baconi.pka.common.xml.XmlDeserializer

fun <T> XmlDeserializer.envelope(innerBody: XmlDeserializer.() -> T): T = tag("Envelope", "Body", innerBody)
