package com.cachedgoods.photoapp.api.users.data;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cachedgoods.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {

	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable("id") String userId);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

	@Override
	public AlbumsServiceClient create(Throwable cause) {

		return new AlbumsServiceClientFallback(cause);
	}
	
}


class AlbumsServiceClientFallback implements AlbumsServiceClient {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final Throwable cause;
	
	public AlbumsServiceClientFallback(Throwable cause) {

		this.cause = cause;
	}

	@Override
	public List<AlbumResponseModel> getAlbums(String userId) {
		
		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error("404 error for getALbums for userId: " + userId +
					", Error message: " + cause.getLocalizedMessage());
		}
		else {
			logger.error("Other error took place: " + cause.getLocalizedMessage());
		}
		
		return new ArrayList<>();
	}
	
}


	// this is for fallback=AlbumsFallback.class
//@Component
//class AlbumsFallback implements AlbumsServiceClient {
//
//	@Override
//	public List<AlbumResponseModel> getAlbums(String userId) {

//		return new ArrayList<>();
//	}
//
//}