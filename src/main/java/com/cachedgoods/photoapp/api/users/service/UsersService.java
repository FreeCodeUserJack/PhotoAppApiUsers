package com.cachedgoods.photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.cachedgoods.photoapp.api.users.shared.UserDto;

public interface UsersService extends UserDetailsService {
	UserDto createUser(UserDto userDetails);
	UserDto findUserDetailsByEmail(String email);
	UserDto getUserByUserId(String userId);
}
