package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

enum class FaultCodes {

    VersionMismatch,
    MustUnderstand,
    Receiver,
    Client,
    Server;

    companion object {
        fun lookup(value: String): FaultCodes? = FaultCodes.values().find {type ->
            type.name.equals(value, ignoreCase = true)
        }
    }
}

data class Fault(
    val code: FaultCodes? = null,
    val reason: String? = null,
    val node: String? = null,
    val role: String? = null,
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
                when (parser.name) {
                    // Fault style 1
                    "Code" -> result = result.copy(code = codeFromXml(parser))
                    "Reason" -> result = result.copy(reason = reasonFromXml(parser))
                    "Node" -> result = result.copy(node = parser.readAsText())
                    "Role" -> result = result.copy(role = parser.readAsText())
                    "Detail" -> result = result.copy(detail = parser.readAsText())

                    // Fault style 2
                    "faultcode" -> result = result.copy(code = parser.readAsText()?.let(FaultCodes.Companion::lookup))
                    "faultstring" -> result = result.copy(reason = parser.readAsText())
                    "faultactor" -> result = result.copy(node = parser.readAsText())
                    "detail" -> result = result.copy(detail = parser.readAsText())
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Fault")

            return result
        }

        /*
            <soap:Code>
                <soap:Value>soap:Receiver</soap:Value>
            </soap:Code>
        */
        private fun codeFromXml(parser: XmlPullParser): FaultCodes? {

            parser.require(XmlPullParser.START_TAG, null, "Fault")

            var result: FaultCodes? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name.toLowerCase()) {
                    "Value" -> result = parser.readAsText()?.let(FaultCodes.Companion::lookup) // TODO - soap:Receiver
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Fault")

            return result
        }

        /*
            <soap:Reason>
                <soap:Text xml:lang="en">Unexpected server error</soap:Text>
            </soap:Reason>
         */
        private fun reasonFromXml(parser: XmlPullParser): String? {

            parser.require(XmlPullParser.START_TAG, null, "Reason")

            var result: String? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name.toLowerCase()) {
                    "Text" -> result = parser.readAsText()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Reason")

            return result
        }
    }
}