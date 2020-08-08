package com.stream.tweet.Properties;

import com.stream.tweet.model.TwitterPropertyModel;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TwitterProperties {

	private static final String twitterPropertyFileName =  "twitter.properties";

	private static File loadTwitterProperties(){
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

	private static Optional<Object> getTwitterProperties() {
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

		return Optional.ofNullable(model);

	}

}
