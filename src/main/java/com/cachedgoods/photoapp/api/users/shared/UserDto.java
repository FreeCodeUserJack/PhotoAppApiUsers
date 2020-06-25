package com.cachedgoods.photoapp.api.users.shared;

import java.io.Serializable;
import java.util.List;

import com.cachedgoods.photoapp.api.users.ui.model.AlbumResponseModel;

public class UserDto implements Serializable {

	private static final long serialVersionUID = 29196680147328600L;

	private String firstName;
	private String lastName;
	private String password;
	private String email;

	private String userId;
	private String encryptedPassword; // service layer will encrypt password provided in initial request

	private List<AlbumResponseModel> albums;

	public List<AlbumResponseModel> getAlbums() {
		return albums;
	}

	public void setAlbums(List<AlbumResponseModel> albums) {
		this.albums = albums;
	}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

}
