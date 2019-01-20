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
    val faultCode: FaultCodes?,
    val faultString: String?,
    val faultActor: String?,
    val detail: String?
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Fault {

            parser.require(XmlPullParser.START_TAG, null, "Fault")

            var faultCode: FaultCodes? = null
            var faultString: String? = null
            var faultActor: String? = null
            var detail: String? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name.toLowerCase()) {
                    "faultcode" -> faultCode = parser.readAsText()?.let(FaultCodes.Companion::lookup)
                    "faultstring" -> faultString = parser.readAsText()
                    "faultactor" -> faultActor = parser.readAsText()
                    "detail" -> detail = parser.readAsText()
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Fault")

            return Fault(faultCode, faultString, faultActor, detail)
        }
    }
}