package com.linkallcloud.core.www.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;

import com.linkallcloud.core.lang.Stopwatch;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.util.IConstants;

public class HttpHealthChecker {

	private static final Log log = Logs.get();

	public static void main(String[] args) throws Exception {
		// boolean flag = isHttpOpen("www.baidu.com", 80);
//        boolean flag = isHealth("edu.mzlink.net", 80, "/questionnaire?clazz=0");
//        System.out.println(flag ? "HTTP服务启动..." : "HTTP服务关闭...");
//
//        boolean flag2 = isHealth("47.96.234.18", 80, "/umyw");
//        System.out.println(flag2 ? "HTTP服务启动..." : "HTTP服务关闭...");
//
//        boolean flag3 = isHealth("localhost", 80, "/lac3-de-view-pc/page/home.html");
//        System.out.println(flag3 ? "HTTP服务启动..." : "HTTP服务关闭...");

		boolean flag4 = isHealth("http://47.96.234.18/umyw/#/dashboard");
		System.out.println(flag4 ? "HTTP服务启动..." : "HTTP服务关闭...");

		boolean flag5 = isHealth("http://www.baidu.com");
		System.out.println(flag5 ? "HTTP服务启动..." : "HTTP服务关闭...");

		boolean flag6 = isHealth("http://edu.mzlink.net/questionnaire?clazz=0");
		System.out.println(flag6 ? "HTTP服务启动..." : "HTTP服务关闭...");

		boolean flag7 = isHealth("http://47.96.234.18/umyw");
		System.out.println(flag7 ? "HTTP服务启动..." : "HTTP服务关闭...");

		boolean flag8 = isHealth(
				"http://47.96.234.18/sso/login?service=http%3A%2F%2Flocalhost%2Fiapi%2Fssoauth%3Fredirect%3Dhttp%253A%252F%252Flocalhost%252Flac3-de-view-pc%252Fpage%252Fhome.html&from=lac_app_de&clazz=0");
		System.out.println(flag8 ? "HTTP服务启动..." : "HTTP服务关闭...");

		boolean flag9 = isHealth("www.baidu.com?t123");
		System.out.println(flag9 ? "HTTP服务启动..." : "HTTP服务关闭...");
		
		boolean flag10 = isHealth("www.baidu1111111111111111.com?t123");
		System.out.println(flag10 ? "HTTP服务启动..." : "HTTP服务关闭...");
		
		
		long duration = responseCost("http://www.baidu.com");
		System.out.println("http://www.baidu.com 响应时间："+(duration<=0?"服务异常":duration));
		
		long duration2 = responseCost("http://47.96.234.18/umyw/#/dashboard");
		System.out.println("http://47.96.234.18/umyw/#/dashboard 响应时间："+(duration2<=0?"服务异常":duration2));
		
		long duration3 = responseCost("http://edu.mzlink.net/questionnaire?clazz=0");
		System.out.println("http://edu.mzlink.net/questionnaire?clazz=0 响应时间："+(duration3<=0?"服务异常":duration3));
		
		long duration4 = responseCost("http://www.baidu1111111111111111.com/?t123");
		System.out.println("http://www.baidu1111111111111111.com/?t123 响应时间："+(duration4<=0?"服务异常":duration4));
	}

	public static Weber parseWeber(String url) {
		// log.debug("########## HttpHealthChecker.parseWeber url : " + url);
		if (Strings.isBlank(url)) {
			return null;
		}

		try {
			url = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}

		String tmp = null;
		if (url.startsWith(IConstants.HTTPS)) {
			tmp = url.substring(8);
		} else if (url.startsWith(IConstants.HTTP)) {
			tmp = url.substring(7);
		} else {
			tmp = url;
		}

		int index1 = tmp.indexOf(IConstants.LEFT_SLASH);// 是否有/
		int index2 = tmp.indexOf(IConstants.QUESTION_MARK);// 是否有?

		int guessLen = -1;
		if (index1 != -1 || index2 != -1) {
			if (index1 == -1) {
				guessLen = index2;
			} else if (index2 == -1) {
				guessLen = index1;
			} else {
				guessLen = index1 < index2 ? index1 : index2;
			}
		}

		String addr = null;
		String host = tmp;
		if (guessLen != -1) {
			host = tmp.substring(0, guessLen);
			addr = tmp.substring(guessLen);
			if (addr.startsWith("?")) {
				addr = "/" + addr;
			}
		}

		String server = null;
		int port = 80;
		int idx = host.indexOf(IConstants.COLON);// 是否有:
		if (idx > 0) {
			server = host.substring(0, idx);
			port = Integer.parseInt(host.substring(idx + 1));
		} else {
			server = host;
		}
		// log.debug("########## HttpHealthChecker.parseWeber Weber : " + server + ", " + port + ", " + addr);
		return new Weber(server, port, addr);
	}

	public static boolean isHealth(String url, int ifFailureRetryTimes) {
		Weber weber = parseWeber(url);
		boolean health = isHealth(weber);
		if (health == false && ifFailureRetryTimes > 0) {
			return isHealth(url, ifFailureRetryTimes - 1);
		} else {
			return health;
		}
	}

	public static boolean isHealth(String url) {
		Weber weber = parseWeber(url);
		return isHealth(weber);
	}

	public static boolean isHealth(Weber weber) {
		if (weber != null) {
			return isHealth(weber.getServer(), weber.getPort(), weber.getAddr());
		}
		return false;
	}

	/**
	 * 判断ip的port端口是否开放了http服务
	 * 
	 * @param ip
	 * @param port
	 * @param url
	 * @return
	 */
	public static boolean isHealth(String ip, int port, String url) {
		if (Strings.isBlank(ip)) {
			return false;
		}
		Socket socket = null; // socket链接
		OutputStream os = null; // 输出流
		BufferedReader br = null; // 输入流
		boolean flag = false; // 是否开启了HTTP服务
		try {
			socket = new Socket(ip, port);
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 创建HTTP请求报文信息
			String addr = Strings.isBlank(url) ? "/" : url;
			String reqInfo = "GET " + addr + " HTTP/1.0" + "\r\n" + "Host: " + ip + ":" + port + "\r\n\r\n";
			log.debug("请求报文 : \r\n" + reqInfo);
			// 发送请求消息
			os.write(reqInfo.getBytes());
			os.flush();
			// 接收响应消息
			String lineStr = null;
			log.debug("响应报文 : ");
			while ((lineStr = br.readLine()) != null) {
				// 读取到了响应信息，表示该ip的port端口提供了HTTP服务
				log.debug(lineStr);
				log.debug("......");
				flag = true;
				break;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (br != null)
					br.close();
				if (os != null)
					os.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return flag;
	}
	
	/**
	 * 响应时间
	 * 
	 * @param url
	 * @return
	 */
	public static long responseCost(String url) {
		Weber weber = parseWeber(url);
		return responseCost(weber);
	}

	/**
	 * 响应时间
	 * 
	 * @param weber
	 * @return
	 */
	public static long responseCost(Weber weber) {
		if (weber != null) {
			return responseCost(weber.getServer(), weber.getPort(), weber.getAddr());
		}
		return -1L;
	}
	
	/**
	 * 响应时间
	 * 
	 * @param ip
	 * @param port
	 * @param url
	 * @return
	 */
	public static long responseCost(String ip, int port, String url) {
		if (Strings.isBlank(ip)) {
			return -1L;
		}
		Socket socket = null; // socket链接
		OutputStream os = null; // 输出流
		BufferedReader br = null; // 输入流
		Stopwatch sw = new Stopwatch();
		try {
			socket = new Socket(ip, port);
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 创建HTTP请求报文信息
			String addr = Strings.isBlank(url) ? "/" : url;
			String reqInfo = "GET " + addr + " HTTP/1.0" + "\r\n" + "Host: " + ip + ":" + port + "\r\n\r\n";
			log.debug("请求报文 : \r\n" + reqInfo);
			// 发送请求消息
			os.write(reqInfo.getBytes());
			os.flush();
			
			sw.start(); // 开启秒表
			// 接收响应消息
			String lineStr = null;
			log.debug("响应报文 : ");
			while ((lineStr = br.readLine()) != null) {
				// 读取到了响应信息，表示该ip的port端口提供了HTTP服务
				sw.stop();
				log.debug(lineStr);
				log.debug("......");
				break;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (br != null)
					br.close();
				if (os != null)
					os.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return sw.getDuration();
	}

}
