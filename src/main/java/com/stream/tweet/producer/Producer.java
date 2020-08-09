package com.stream.tweet.producer;

import com.stream.tweet.constants.Application;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {

	public KafkaProducer<String, String> createKafkaProducer(){

		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Application.BOOTSTRAP_SERVERS);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


		// safe producer
		properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
		properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
		// retries, if using a key, then there will be no order guarantee
		// max.in.flight.requests.per.connection should be set to 1

		//Idempotent Producer
		// if producer sends the data and the data is committed, but producer never receives an ack
		// producer tries again, there will be duplicate commits.
		/*
		This ensure that that there are no duplicate commits happening.
		retries, max.in.flight.requests, acks=all
		"enable.idempotence", true
		 */

		// compression

		// linger.ms and batch.size
		/*
		how long to wait before sending the message to kafka
		what is the max size of total message we can hold within the timeframe of linger.ms
		 */
		properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy");
		properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
		properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024));

		//create producer
		return new KafkaProducer<>(properties);

	}

}
