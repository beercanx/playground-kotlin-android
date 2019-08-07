package uk.co.baconi.pka.common.xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import org.xmlpull.v1.XmlSerializer

object XmlParserFactory {

    private val xmlParserFactory = XmlPullParserFactory.newInstance()

    fun deserializer(): XmlPullParser = xmlParserFactory.newPullParser().apply {
        setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
    }

    fun serializer(): XmlSerializer = xmlParserFactory.newSerializer()

}