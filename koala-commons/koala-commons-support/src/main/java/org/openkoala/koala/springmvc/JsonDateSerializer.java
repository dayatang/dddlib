package org.openkoala.koala.springmvc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * 
 * 
 * @description SpringMVC对日期类型的支持
 *  
 * @date：      2013-8-16   
 * 
 * @version    Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author     lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public class JsonDateSerializer extends JsonSerializer<Date> {

	/**
	 * 序列化日期类型
	 */
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String formattedDate = dateFormat.format(date);

        gen.writeString(formattedDate);
    }//java.sql.Timestamp

}
