
package org.jboss.brms.filter;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 供RequestEncodingFilter使用。
 * 作用是转换一个HttpServletRequest，转换其取得参数的函数，避免出现乱码
 * 
 */
@SuppressWarnings("rawtypes")
public class RequestEncodingWrapper extends HttpServletRequestWrapper {

    private String encoding;

    /**
     * @param request
     */
    public RequestEncodingWrapper(HttpServletRequest request) {
        this(request, "utf-8");
    }

    public RequestEncodingWrapper(HttpServletRequest request, String enc) {
        super(request);
        encoding = enc;
    }

    private static String getEncodedString(String value, String enc)
            throws UnsupportedEncodingException {
        return new String(value.getBytes("ISO-8859-1"), enc);
    }

    @Override
    public String getParameter(String key) {
        String ret = super.getParameter(key);
        if (ret != null) {
            try {
                ret = getEncodedString(ret, encoding);
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public Map getParameterMap() {

        Map map = super.getParameterMap();

        if (map != null) {

            Iterator it = map.keySet().iterator();

            while (it.hasNext()) {
                Object obj = map.get(it.next());
                if (obj != null) {
                    try {
                        obj = getEncodedString((String) obj, encoding);
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                    }
                } // if (obj != null)
            } // while (it.hasNext)
        } // if (map != null)
        return map;
    }

    @Override
    public String[] getParameterValues(String key) {

        String[] ret = super.getParameterValues(key);

        if (ret != null) {
            for (int i = 0; i < ret.length; i++) {
                try {
                    ret[i] = getEncodedString(ret[i], encoding);
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
    
            }
        }

        return ret;
    }

}
