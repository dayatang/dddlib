package org.openkoala.jbpm.core.jbpm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Map;

public class MapSerializeUtil {

	public static byte[] serialize(Map<String, Object> vas) {
		try {
			ByteArrayOutputStream mem_out = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(mem_out);

			out.writeObject(vas);

			out.close();
			mem_out.close();

			byte[] bytes = mem_out.toByteArray();
			return bytes;
		} catch (IOException e) {
			return null;
		}
	}

	public static Map<String, Object> deserialize(byte[] bytes) {
		try {
			ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(mem_in);

			Map<String, Object> hashMap = (Map<String, Object>) in
					.readObject();
			in.close();
			mem_in.close();
			return hashMap;
		} catch (StreamCorruptedException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}
