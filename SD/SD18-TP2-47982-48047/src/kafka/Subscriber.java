package kafka;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import api.storage.Datanode;
import api.storage.Namenode;
import sys.storage.DatanodeClient;
import sys.storage.NamenodeClient;

public class Subscriber {

	private static final long TIMEOUT = 10000;
	
	public Subscriber(Map<String, Namenode> namenodes ,Map<String, Datanode> datanodes ) {
		Thread subscrive = new Thread() {
			public void run() {
		Random rnd = new Random();
		
		Properties props = new Properties();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092,kafka2:9092,kafka3:9092");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "grp" + rnd.nextLong());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		
		
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
			consumer.subscribe(Arrays.asList("Discovery"));
			
			Logger.getAnonymousLogger().log(Level.INFO, "Ligado ao Kafka; Esperando por eventos...");
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(TIMEOUT);
				records.forEach(r -> {
				try {
					if (r.key().equals(("Namenode"))) {
						if(!namenodes.containsKey(r.value())) {
						System.out.println("Namenode"+r.key() +" " +r.value());
						namenodes.put(r.value(), new NamenodeClient(r.value()));
						}
					} else  {
						if(!datanodes.containsKey(r.value())) {
						System.out.println("Datanode"+r.key() +" " +r.value());
						datanodes.put(r.value(), new DatanodeClient(r.value()));
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}});
			}
		}
			}
	};
	subscrive.start();
	}
	
}
