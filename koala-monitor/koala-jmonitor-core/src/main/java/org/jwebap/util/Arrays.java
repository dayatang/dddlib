package org.jwebap.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 数组操作类
 * 
 * 由于jdk14的数组操作类在toString格式化方面功能不足，所以这里采用了jdk15的格式化方式实现
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date 2008-2-4
 */
public class Arrays {
	
	/**
	 * 数组增加
	 * @param arr
	 * @param val
	 */
	public static void add(Object[] arr,Object val){
		Object[] newArr=new Object[arr.length+1];
		
		System.arraycopy(arr,0,newArr, 0,arr.length);
		
		newArr[arr.length]=val;
		
	}
	
	public static String toString(long[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(int[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(short[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(char[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(byte[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(boolean[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(float[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(double[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();
		buf.append('[');
		buf.append(a[0]);

		for (int i = 1; i < a.length; i++) {
			buf.append(", ");
			buf.append(a[i]);
		}

		buf.append("]");
		return buf.toString();
	}

	public static String toString(Object[] a) {
		if (a == null)
			return "null";
		if (a.length == 0)
			return "[]";

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < a.length; i++) {
			if (i == 0)
				buf.append('[');
			else
				buf.append(", ");

			buf.append(String.valueOf(a[i]));
		}

		buf.append("]");
		return buf.toString();
	}

	public static String deepToString(Object[] a) {
		if (a == null)
			return "null";

		int bufLen = 20 * a.length;
		if (a.length != 0 && bufLen <= 0)
			bufLen = Integer.MAX_VALUE;
		StringBuffer buf = new StringBuffer(bufLen);
		deepToString(a, buf, new HashSet());
		return buf.toString();
	}

	private static void deepToString(Object[] a, StringBuffer buf, Set dejaVu) {
		if (a == null) {
			buf.append("null");
			return;
		}
		dejaVu.add(a);
		buf.append('[');
		for (int i = 0; i < a.length; i++) {
			if (i != 0)
				buf.append(", ");

			Object element = a[i];
			if (element == null) {
				buf.append("null");
			} else {
				Class eClass = element.getClass();

				if (eClass.isArray()) {
					if (eClass == byte[].class)
						buf.append(toString((byte[]) element));
					else if (eClass == short[].class)
						buf.append(toString((short[]) element));
					else if (eClass == int[].class)
						buf.append(toString((int[]) element));
					else if (eClass == long[].class)
						buf.append(toString((long[]) element));
					else if (eClass == char[].class)
						buf.append(toString((char[]) element));
					else if (eClass == float[].class)
						buf.append(toString((float[]) element));
					else if (eClass == double[].class)
						buf.append(toString((double[]) element));
					else if (eClass == boolean[].class)
						buf.append(toString((boolean[]) element));
					else { // element is an array of object references
						if (dejaVu.contains(element))
							buf.append("[...]");
						else
							deepToString((Object[]) element, buf, dejaVu);
					}
				} else { // element is non-null and not an array
					buf.append(element.toString());
				}
			}
		}
		buf.append("]");
		dejaVu.remove(a);
	}
}
