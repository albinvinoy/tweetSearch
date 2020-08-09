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
		// add shutdown hoook
		Runtime.getRuntime().addShutdownHook(new Thread( () -> {
			System.out.println("stopping Application...");
			kafkaProducer.flush();
//			kafkaProducer.close();
			twitterClient.stop();
		}));

		// check kafka version same as client version
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
				kafkaProducer.send(new ProducerRecord<>("twitter_trump", null, msg), (messageMetaData, e) ->{
					if(e!=null){
						System.err.println("Something wrong with kafka producer");
						e.printStackTrace();
						kafkaProducer.close();
					}
				});

			}
		}
	}
}
