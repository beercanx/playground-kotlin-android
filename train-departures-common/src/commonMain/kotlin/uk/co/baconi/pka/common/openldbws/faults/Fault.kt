package uk.co.baconi.pka.common.openldbws.faults

import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.parse
import uk.co.baconi.pka.common.xml.readAsText
import uk.co.baconi.pka.common.xml.skip

data class Fault(val code: FaultCodes? = null, val reason: String? = null) : Exception(
    "OpenLDBWS returned a fault of [$code] with reason [$reason]"
) {
    companion object {

        fun XmlDeserializer.fault(): Fault = parse("Fault") { result, prefix ->
            when (getName()) {
                // Fault style 1
                "Code" -> result.copy(code = code())
                "Reason" -> result.copy(reason = reason())

                // Fault style 2
                "faultcode" -> result.copy(code = code(prefix))
                "faultstring" -> result.copy(reason = readAsText())
                else -> skip(result)
            }
        }

        /*
            <soap:Code>
                <soap:Value>soap:Receiver</soap:Value>
            </soap:Code>
        */
        private fun XmlDeserializer.code(): FaultCodes? = parse("Code", null as FaultCodes?) { result ->
            when (getName()) {
                "Value" -> code(getPrefix())
                else -> skip(result)
            }
        }

        /*
            <faultcode>soap:Client</faultcode>
        */
        private fun XmlDeserializer.code(prefix: String?): FaultCodes? {
            return readAsText()
                ?.replace("$prefix:", "")
                ?.let(FaultCodes.Companion::lookup)
        }

        /*
            <soap:Reason>
                <soap:Text xml:lang="en">Unexpected server error</soap:Text>
            </soap:Reason>
        */
        private fun XmlDeserializer.reason(): String? = parse("Reason", null as String?) { result ->
            when (getName()) {
                "Text" -> readAsText()
                else -> skip(result)
            }
        }
    }
}