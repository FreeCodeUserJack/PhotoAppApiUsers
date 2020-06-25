package com.cachedgoods.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cachedgoods.photoapp.api.users.data.AlbumsServiceClient;
import com.cachedgoods.photoapp.api.users.data.UserEntity;
import com.cachedgoods.photoapp.api.users.data.UsersRepository;
import com.cachedgoods.photoapp.api.users.shared.UserDto;
import com.cachedgoods.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;
import net.bytebuddy.asm.Advice.This;

@Service
public class UsersServiceImpl implements UsersService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final UsersRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
//	@Autowired
//	@LoadBalanced
//	RestTemplate restTemplate;
	
	private final Environment environment;
	
	private final AlbumsServiceClient albumsServiceClient;

	// don't need @Autowired if there is only 1 constructor
	public UsersServiceImpl(UsersRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Environment envi,
			AlbumsServiceClient albumsServiceClient) {
		this.albumsServiceClient = albumsServiceClient;
		this.environment = envi;
//		this.restTemplate = new RestTemplate();
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		// can't use database id, need to create UUID
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userDetails, UserEntity.class);
		
		UserEntity returnEntity = userRepository.save(userEntity);
		
		return mapper.map(returnEntity, UserDto.class);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity returnUser = userRepository.findByEmail(username);
		
		if (returnUser == null) throw new UsernameNotFoundException("Username not found: " + username);
		
		return new User(returnUser.getEmail(), returnUser.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>()
				);
	}

	@Override
	public UserDto findUserDetailsByEmail(String email) {
		
		UserEntity returnUser = userRepository.findByEmail(email);
		
		if (returnUser == null) throw new UsernameNotFoundException("Username not found: " + email);
		
		return new ModelMapper().map(returnUser, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) throw new UsernameNotFoundException("User Not Found");
		
		UserDto returnValue = new ModelMapper().map(userEntity, UserDto.class);
		
//		String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
		// null below is HttpEntity, we don't need b/c no headers, etc.
//		ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//		});
//		List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
		
		
//		List<AlbumResponseModel> albumsList = null;
//		
//		try {
//			albumsList = albumsServiceClient.getAlbums(userId);
//		}
//		catch (FeignException e) {
//			logger.error(e.getLocalizedMessage());;
//		}
		
		logger.info("Before calling albums Microservice");
		List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
		logger.info("After calling albums Microservice");
		
		returnValue.setAlbums(albumsList);
		
		return returnValue;
	}

}
