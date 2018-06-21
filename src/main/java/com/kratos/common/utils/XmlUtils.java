package com.kratos.common.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by He on 2017/5/27.
 */
public class XmlUtils {
    /**
     * map转换为xml
     *
     * @param map
     * @return
     */
    public static String mapToXml(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (Map.Entry<String, Object> m : map.entrySet()) {
            sb.append("<" + m.getKey() + ">");

            sb.append(m.getValue());
            sb.append("</" + m.getKey() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * xml转换为map
     *
     * @param xml
     * @return
     */
    public static Map<String, Object> xmltoMap(String xml) throws XmlPullParserException, IOException {
        Map<String, Object> map = new HashMap<>();
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(new StringReader(xml));
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {

            String nodeName = parser.getName();
            switch (event) {
                case XmlPullParser.START_DOCUMENT:

                    break;
                case XmlPullParser.START_TAG:
                    if ("xml".equals(nodeName) == false) {
                        map.put(nodeName, parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            event = parser.next();
        }
        return map;
    }
}
