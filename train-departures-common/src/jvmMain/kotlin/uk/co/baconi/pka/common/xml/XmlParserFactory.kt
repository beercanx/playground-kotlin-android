package uk.co.baconi.pka.common.xml

import org.xmlpull.v1.XmlPullParserFactory
import org.xmlpull.v1.XmlSerializer

object XmlParserFactory {

    private val xmlParserFactory = XmlPullParserFactory.newInstance()

    fun serializer(): XmlSerializer = xmlParserFactory.newSerializer()

}