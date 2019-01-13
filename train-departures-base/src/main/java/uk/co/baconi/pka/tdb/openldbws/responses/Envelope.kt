package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import uk.co.baconi.pka.tdb.xml.skip
import java.io.InputStream

data class Envelope(val body: Body?) {

    companion object {

        private val xmlParser = XmlPullParserFactory.newInstance()

        fun fromInputStream(inputStream: InputStream): Envelope? {
            inputStream.use { openedStream ->
                val parser: XmlPullParser = xmlParser.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(openedStream, "UTF-8")
                parser.nextTag()
                return fromXml(parser)
            }
        }

        private fun fromXml(parser: XmlPullParser): Envelope? {

            parser.require(XmlPullParser.START_TAG, null, "soap:Envelope")

            var body: Body? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "soap:Body" -> body = Body.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "soap:Envelope")

            return Envelope(body)
        }
    }
}