package com.stream.tweet.Properties;

import com.stream.tweet.model.TwitterPropertyModel;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TwitterProperties {

	private static final String twitterPropertyFileName =  "twitter.properties";
	private static TwitterProperties TWITTER_PROPERTIES = null;

	private TwitterProperties() {}

	public static TwitterProperties getInstance(){
		if(TWITTER_PROPERTIES == null){
			synchronized (TwitterProperties.class){
				TWITTER_PROPERTIES = new TwitterProperties();
			}
		}
		return TWITTER_PROPERTIES;
	}

	private File loadTwitterProperties(){
		ClassLoader classLoader = new ClassLoader() {
			@Override
			public String getName() {
				return twitterPropertyFileName;
			}
		};

		URL resource = classLoader.getResource(twitterPropertyFileName);
		if(resource == null){
			throw new IllegalArgumentException("Failed to load twitter.properties");
		}
		else{
			return new File(resource.getFile());
		}

	}

	public Optional<TwitterPropertyModel> getTwitterProperties() {
		Map<String, String> properties = new HashMap<>();

		try(
				FileReader reader = new FileReader(loadTwitterProperties());
				BufferedReader br = new BufferedReader(reader)) {
			String line ;
			while( (line = br.readLine()) != null){
				String[] property = line.split("=");
				properties.put(property[0], property[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		TwitterPropertyModel model = TwitterPropertyModel.builder()
				.accessToken(properties.get("accessToken"))
				.accessTokenSecret(properties.get("accessTokenSecret"))
				.apiKey(properties.get("apiKey"))
				.apiSecret(properties.get("apiSecret"))
				.bearerToken(properties.get("bearerToken"))
				.build();

		return Optional.of(model);

	}

}
