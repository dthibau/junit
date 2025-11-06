package org.formation.service;

import lombok.Data;

@Data
public class UserDto {

	
	private String login,password;
	
	public UserDto(String login, String password) {
		this.login = login;
		this.password = password;
	}
}
