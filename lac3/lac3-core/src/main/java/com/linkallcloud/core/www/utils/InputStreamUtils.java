package com.linkallcloud.core.www.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;

public class InputStreamUtils {

	private static Log log = Logs.get();
	final static int BUFFER_SIZE = 4096;

	public static String convertStreamToString(InputStream is, String encoding) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			/*
			 * To convert the InputStream to String we use the BufferedReader.readLine()
			 * method. We iterate until the BufferedReader return null which means there's
			 * no more data to read. Each line will appended to a StringBuilder and returned
			 * as String.
			 */
			reader = new BufferedReader(new InputStreamReader(is, Strings.isBlank(encoding) ? "UTF-8" : encoding));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				is.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return sb.toString();
	}

	public static String convertStreamToString(InputStream is) {
		return convertStreamToString(is, "UTF-8");
	}

	/**
	 * @param in
	 * @param encoding
	 * @return String
	 * @throws Exception
	 */
	public static String InputStreamTOString(InputStream in, String encoding) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), Strings.isBlank(encoding) ? "UTF-8" : encoding);
	}

	/**
	 * @param in
	 * @return String
	 * @throws Exception
	 */
	public static String InputStreamTOString(InputStream in) throws Exception {
		return InputStreamTOString(in, "UTF-8");
	}

	/**
	 * @param in
	 * @param encoding
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream StringTOInputStream(String in, String encoding) throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes(Strings.isBlank(encoding) ? "UTF-8" : encoding));
		return is;
	}

	/**
	 * @param in
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream StringTOInputStream(String in) throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
		return is;
	}
}
