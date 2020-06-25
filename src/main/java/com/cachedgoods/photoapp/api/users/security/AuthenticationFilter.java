package com.cachedgoods.photoapp.api.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cachedgoods.photoapp.api.users.service.UsersService;
import com.cachedgoods.photoapp.api.users.shared.UserDto;
import com.cachedgoods.photoapp.api.users.ui.model.LoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final UsersService usersService;
	private final Environment environment;

	public AuthenticationFilter(UsersService usersService, Environment environment,
			AuthenticationManager authManager) {
		this.usersService = usersService;
		this.environment = environment;
		super.setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {

		try {

			LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);

			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

		} catch (IOException e) {

			throw new RuntimeException();
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String userName = ((User) auth.getPrincipal()).getUsername();
		UserDto returnUser = usersService.findUserDetailsByEmail(userName);
		
		System.out.println(
				environment.getProperty("token.expiration_date") + "\n"
				+ environment.getProperty("token.expiration_date") + "\n"
				+ environment.getProperty("token.expiration_date") + "\n"
				+ environment.getProperty("token.expiration_date") + "\n"
				+ environment.getProperty("token.expiration_date") + "\n");
		
		String token = Jwts.builder()
				.setSubject(returnUser.getUserId())
				.setExpiration(new Date(System.currentTimeMillis() +
						Long.parseLong(environment.getProperty("token.expiration_date"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				.compact();
		
		res.addHeader("token", token);
		res.addHeader("userId", returnUser.getUserId());
	}

}
