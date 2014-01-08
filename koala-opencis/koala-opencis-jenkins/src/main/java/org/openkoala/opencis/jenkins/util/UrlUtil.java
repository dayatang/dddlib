package org.openkoala.opencis.jenkins.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 5:50 PM
 */
public class UrlUtil {

    public static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encode " + url + " to UTF-8 failure", e);
        }
    }
}
