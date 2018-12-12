/**
 * 
 */
package com.synectiks.demo.site.dto;

/**
 * @author Rajesh
 */
public class Login {

	private String username;
	private String password;
	private String redirect;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		if (username != null) {
			builder.append("\"username\": \"" + username + "\"");
		}
		if (password != null) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"password\": \"" + password + "\"");
		}
		if (redirect != null) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"redirect\": \"" + redirect + "\"");
		}
		builder.append("}");
		return builder.toString();
	}

}
