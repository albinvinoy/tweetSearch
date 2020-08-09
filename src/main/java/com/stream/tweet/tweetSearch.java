package com.stream.tweet;

import com.stream.tweet.client.TwitterStreamingApi;
import com.twitter.hbc.core.Client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class tweetSearch {
	public static void main(String[] args) {

		System.out.println("Hello tweet search here!");
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>();
		TwitterStreamingApi twitterStreamingApi = TwitterStreamingApi.getInstance();

		Client twitterClient = twitterStreamingApi.getTwitterClient(msgQueue);
		twitterClient.connect();

		while (!twitterClient.isDone()) {
			String msg = "";
			try {
				msg = msgQueue.poll(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				twitterClient.stop();
			}
			System.out.println(msg);
		}
	}
}
