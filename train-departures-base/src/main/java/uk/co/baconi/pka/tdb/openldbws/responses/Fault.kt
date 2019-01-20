package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

enum class FaultCodes {

    VersionMismatch,
    MustUnderstand,
    Client,
    Server;

    companion object {
        fun lookup(value: String): FaultCodes? = FaultCodes.values().find {type -> type.name.contains(value) }
    }
}

data class Fault(
    val faultCode: FaultCodes? = null,
    val faultString: String? = null,
    val faultActor: String? = null,
    val detail: String? = null
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Fault {

            parser.require(XmlPullParser.START_TAG, null, "Fault")

            var result = Fault()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name.toLowerCase()) { // TODO - Update to work with NRE style
                    "faultcode" -> result = result.copy(faultCode = parser.readAsText()?.let(FaultCodes.Companion::lookup))
                    "faultstring" -> result = result.copy(faultString = parser.readAsText())
                    "faultactor" -> result = result.copy(faultActor = parser.readAsText())
                    "detail" -> result = result.copy(detail = parser.readAsText())
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Fault")

            return result
        }
    }
}