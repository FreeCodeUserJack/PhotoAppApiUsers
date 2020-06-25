package com.cachedgoods.photoapp.api.users.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {
	
	@NotNull(message = "First Name Cannot Be Null")
	@Size(min=2, max=25, message = "First Name must be between 2 and 25 chars")
	private String firstName;
	
	@NotNull(message = "Last Name Cannot Be Null")
	@Size(min=2, max=25, message = "Last Name must be between 2 and 25 chars")
	private String lastName;
	
	@NotNull(message = "Password Cannot Be Null")
	@Size(min = 8, max = 16, message = "Password must be between 8 to 16 chars long")
	private String password;
	
	@NotNull(message = "Email Cannot Be Null")
	@Email(message = "Please enter a valid email address")
	private String email;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
