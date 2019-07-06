package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

enum class FaultCodes {

    VersionMismatch,
    MustUnderstand,
    Receiver,
    Client,
    Server;

    companion object {
        fun lookup(value: String): FaultCodes? = values().find { type ->
            type.name.equals(value, ignoreCase = true)
        }
    }
}

data class Fault(val code: FaultCodes? = null, val reason: String? = null) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Fault = parser.parse("Fault") { result, prefix ->
            when (parser.name) {
                // Fault style 1
                "Code" -> result.copy(code = codeFromXml(parser))
                "Reason" -> result.copy(reason = reasonFromXml(parser))

                // Fault style 2
                "faultcode" -> result.copy(code = codeFromXml(parser, prefix))
                "faultstring" -> result.copy(reason = parser.readAsText())
                else -> parser.skip(result)
            }
        }

        /*
            <faultcode>soap:Client</faultcode>
         */
        private fun codeFromXml(parser: XmlPullParser, prefix: String?): FaultCodes? {
            return parser.readAsText()
                ?.replace("$prefix:", "")
                ?.let(FaultCodes.Companion::lookup)
        }

        /*
            <soap:Code>
                <soap:Value>soap:Receiver</soap:Value>
            </soap:Code>
        */
        private fun codeFromXml(parser: XmlPullParser): FaultCodes? = parser.parse("Code", null as FaultCodes?) { result ->
            when (parser.name) {
                "Value" -> codeFromXml(parser, parser.prefix)
                else -> parser.skip(result)
            }
        }

        /*
            <soap:Reason>
                <soap:Text xml:lang="en">Unexpected server error</soap:Text>
            </soap:Reason>
         */
        private fun reasonFromXml(parser: XmlPullParser): String? = parser.parse("Reason", null as String?) { result ->
            when (parser.name) {
                "Text" -> parser.readAsText()
                else -> parser.skip(result)
            }
        }
    }
}