package test.com.linkallcloud.web.utils;

import static org.junit.Assert.assertEquals;

import java.net.URLDecoder;

import org.junit.Test;

import com.alibaba.fastjson.JSON;


public class ControllersTest {
	
	@Test
	public void test1() throws Exception {
		String token = "fishingboat%7ClQCp77TDbTCW4s0aceNqC4vJKkRFzzfMxDzTQBrD6vKY96C%2BhvfBfov1vh1ebR0avbZE2nz4qvYq%0ADJ2RXHCHijeB9y4hXrVFBi%2FA4zLr4QhBsMksxxwbxVhJ30Nlo4Ywx%2BJrLy0HGMxhHYOjFQV26FCn%0AmsX4JkfCGnfmDX4mqpBJgFFcH2mIeWy579NfIaoo%7C502d7a61112d0d589ba945ffce47a18bd06be24e";
		token = URLDecoder.decode(token, "UTF-8");
		TestUser tu = new TestUser(100L, token);
		
		System.out.println(JSON.toJSONString(tu));
	}
	
	//String jsonStr = "{\"id\":100,\"token\":\"fishingboat%7ClQCp77TDbTCW4s0aceNqC4vJKkRFzzfMxDzTQBrD6vKY96C%2BhvfBfov1vh1ebR0avbZE2nz4qvYq%0ADJ2RXHCHijeB9y4hXrVFBi%2FA4zLr4QhBsMksxxwbxVhJ30Nlo4Ywx%2BJrLy0HGMxhHYOjFQV26FCn%0AmsX4JkfCGnfmDX4mqpBJgFFcH2mIeWy579NfIaoo%7C502d7a61112d0d589ba945ffce47a18bd06be24e\"}";
	
	@Test
	public void test2() throws Exception {
		String jsonStr = "{\"id\":100,\"token\":\"fishingboat|lQCp77TDbTCW4s0aceNqC4vJKkRFzzfMxDzTQBrD6vKY96C+hvfBfov1vh1ebR0avbZE2nz4qvYq\\nDJ2RXHCHijeB9y4hXrVFBi/A4zLr4QhBsMksxxwbxVhJ30Nlo4Ywx+JrLy0HGMxhHYOjFQV26FCn\\nmsX4JkfCGnfmDX4mqpBJgFFcH2mIeWy579NfIaoo|502d7a61112d0d589ba945ffce47a18bd06be24e\"}";
		System.out.println(jsonStr);
		
		TestUser tu = JSON.parseObject(jsonStr, TestUser.class);
		
		String jsonStr2 = JSON.toJSONString(tu);
		assertEquals(jsonStr, jsonStr2);
		
		System.out.println(jsonStr2);
	}
	
	

}
