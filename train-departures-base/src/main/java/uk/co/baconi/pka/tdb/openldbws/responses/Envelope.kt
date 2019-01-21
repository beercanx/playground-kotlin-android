package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import uk.co.baconi.pka.tdb.xml.skip
import java.io.InputStream
import java.io.Reader

data class Envelope(val body: Body? = null) {

    companion object {

        private val xmlParser = XmlPullParserFactory.newInstance()

        fun fromReader(reader: Reader): Envelope {
            reader.use { openedReader ->
                return fromInput {
                    setInput(openedReader)
                }
            }
        }

        fun fromInputStream(inputStream: InputStream): Envelope {
            inputStream.use { openedStream ->
                return fromInput {
                    setInput(openedStream, "UTF-8")
                }
            }
        }

        private fun fromInput(inner: XmlPullParser.() -> Unit): Envelope {
            val parser: XmlPullParser = xmlParser.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
            inner(parser)
            parser.nextTag()
            return fromXml(parser)
        }

        private fun fromXml(parser: XmlPullParser): Envelope {

            parser.require(XmlPullParser.START_TAG, null, "Envelope")

            var result = Envelope()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "Body" -> result = result.copy(body = Body.fromXml(parser))
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Envelope")

            return result
        }
    }
}