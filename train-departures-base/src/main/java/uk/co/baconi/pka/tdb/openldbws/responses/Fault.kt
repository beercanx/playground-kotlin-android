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

data class Fault(val code: FaultCodes? = null, val reason: String? = null) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Fault {

            parser.require(XmlPullParser.START_TAG, null, "Fault")

            val prefix = parser.prefix

            var result = Fault()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    // Fault style 1
                    "Code" -> result = result.copy(code = codeFromXml(parser))
                    "Reason" -> result = result.copy(reason = reasonFromXml(parser))

                    // Fault style 2
                    "faultcode" -> result = result.copy(code = codeFromXml(parser, prefix))
                    "faultstring" -> result = result.copy(reason = parser.readAsText())
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Fault")

            return result
        }


        /*
            <faultcode>soap:Client</faultcode>
         */
        private fun codeFromXml(parser: XmlPullParser, prefix: String): FaultCodes? {
            return parser.readAsText()
                ?.replace("$prefix:", "")
                ?.let(FaultCodes.Companion::lookup)
        }

        /*
            <soap:Code>
                <soap:Value>soap:Receiver</soap:Value>
            </soap:Code>
        */
        private fun codeFromXml(parser: XmlPullParser): FaultCodes? {

            parser.require(XmlPullParser.START_TAG, null, "Code")

            var result: FaultCodes? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "Value" -> result = codeFromXml(parser, parser.prefix)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Code")

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
                when (parser.name) {
                    "Text" -> result = parser.readAsText()
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Reason")

            return result
        }
    }
}