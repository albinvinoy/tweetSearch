package com.stream.tweet.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class TwitterPropertyModel {
	private String apiKey;
	private String apiSecret;
	private String bearerToken;
	private String accessToken;
	private String accessTokenSecret;

}
