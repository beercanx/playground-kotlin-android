package uk.co.baconi.pka.tdb.xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import org.xmlpull.v1.XmlSerializer
import java.io.InputStream
import java.io.Reader

object XmlParser {

    private val xmlParserFactory = XmlPullParserFactory.newInstance()

    fun fromReader(reader: Reader): XmlPullParser {
        reader.use { openedReader ->
            return fromInput {
                setInput(openedReader)
            }
        }
    }

    fun fromInputStream(inputStream: InputStream): XmlPullParser {
        inputStream.use { openedStream ->
            return fromInput {
                setInput(openedStream, "UTF-8")
            }
        }
    }

    private fun fromInput(inner: XmlPullParser.() -> Unit): XmlPullParser {
        val parser: XmlPullParser = xmlParserFactory.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
        inner(parser)
        parser.nextTag()
        return parser
    }

    fun serializer(): XmlSerializer = xmlParserFactory.newSerializer()
}