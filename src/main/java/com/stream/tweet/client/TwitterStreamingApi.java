package com.stream.tweet.client;

import com.google.common.collect.Lists;
import com.stream.tweet.Properties.TwitterProperties;
import com.stream.tweet.model.TwitterPropertyModel;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterStreamingApi {

	BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100);



	public BasicClient getTwitterClient() {

		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		// Optional: set up some followings and track terms
		List<String> terms = Lists.newArrayList("twitter", "api");
		hosebirdEndpoint.trackTerms(terms);

		TwitterProperties properties = TwitterProperties.getInstance();

		Optional<TwitterPropertyModel> twitterProperty = properties.getTwitterProperties();

		Authentication hosebirdAuth;
		if (twitterProperty.isPresent()) {
			hosebirdAuth = new OAuth1(twitterProperty.get().getApiKey(),
					twitterProperty.get().getApiSecret(),
					twitterProperty.get().getAccessToken(),
					twitterProperty.get().getAccessTokenSecret());

		}
		else{
			throw new IllegalArgumentException("Unable to retrieve data from twitter property");
		}

		ClientBuilder horsebirdBuilder = new ClientBuilder()
				.name("Horsebird-Client-01")
				.hosts(hosebirdHosts)
				.authentication(hosebirdAuth)
				.endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue));


		return horsebirdBuilder.build();
	}

}
