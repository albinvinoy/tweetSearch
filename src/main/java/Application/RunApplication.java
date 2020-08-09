package Application;

import com.stream.tweet.client.TwitterStreamingApi;
import com.stream.tweet.producer.Producer;
import com.twitter.hbc.core.Client;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import javax.swing.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class RunApplication {

	public void run(){
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>();
		TwitterStreamingApi twitterStreamingApi = TwitterStreamingApi.getInstance();
		Producer producer = new Producer();

		KafkaProducer<String, String> kafkaProducer = producer.createKafkaProducer();

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
			if(msg != null){
				try {
					kafkaProducer.send(new ProducerRecord<>("twitter_trump", null, msg)).get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
