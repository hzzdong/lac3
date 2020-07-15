package test.com.linkallcloud.web.utils;

public class TestUser {
	private Long id;
	private String token;

	public TestUser() {
		super();
	}

	public TestUser(Long id, String token) {
		super();
		this.id = id;
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
