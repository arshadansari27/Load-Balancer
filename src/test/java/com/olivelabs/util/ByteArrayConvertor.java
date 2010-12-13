package com.olivelabs.util;

public class ByteArrayConvertor {
	public static String convertToString(byte[] data){
		StringBuilder builder = new StringBuilder();
		for(int index = 0; index < data.length; index++){
			builder.append((char)data[index]);
		}
		return builder.toString();
	}
}
