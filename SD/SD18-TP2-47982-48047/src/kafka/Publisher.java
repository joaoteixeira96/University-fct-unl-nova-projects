package kafka;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Publisher extends Thread {
	private String uri, servico;
	private String key;

	public Publisher(String servico, String key, String uri) {
		this.uri = uri;
		this.servico = servico;
		this.key = key;
	}
	@Override
	public void run() {
		for(;;) {
		Properties props = new Properties();
		
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092,kafka2:9092,kafka3:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

		Logger.getAnonymousLogger().log(Level.INFO, "Ligado ao Kafka; Dando inicio ao envio de eventos...");

		try (Producer<String, String> producer = new KafkaProducer<>(props)) {
				producer.send(new ProducerRecord<String, String>(servico,key,
						uri));
		}
		sleep(5000);
		}
	}
	static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception x) {
		}
	}
}

