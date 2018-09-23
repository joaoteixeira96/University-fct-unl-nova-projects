package sys.storage;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import api.storage.Datanode;
import api.storage.Namenode;
import kafka.Publisher;
import mongo.Mongo;
import sys.storage.DatanodeClient;
import utils.IP;
import utils.ServiceDiscovery;

public class NamenodeRest implements Namenode {

	private static final String NAMENODE_PORT_DEFAULT = "7777";

	private static Logger logger = Logger.getLogger(NamenodeRest.class.toString());

	private Mongo mongo;

	Map<String, Datanode> datanodes;

	Set<String> registeredBlocks;

	public NamenodeRest() throws InterruptedException {
		registeredBlocks = new ConcurrentSkipListSet<String>();
		datanodes = new ConcurrentHashMap<String, Datanode>();
		mongo = new Mongo();

			Thread dataNodeDiscovery = new Thread() {
				public void run() {
					while (true) {
						String[] datanodeNames = ServiceDiscovery.multicastSend(ServiceDiscovery.DATANODE_SERVICE_NAME);
						if (datanodeNames != null) {
							for (String datanode : datanodeNames) {
								if (!datanodes.containsKey(datanode)) {
									logger.info("New Datanode discovered: " + datanode);
									try {
										datanodes.put(datanode, new DatanodeClient(datanode));
									} catch (NoSuchAlgorithmException e) {
										e.printStackTrace();
									}
								}
							}
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// nothing to be done
							e.printStackTrace();
						}
					}
				}
			};
			dataNodeDiscovery.start();
		}
	

	@Override
	public synchronized List<String> list(String prefix) {
		return mongo.list(prefix);
	}

	@Override
	public synchronized void create(String name, List<String> blocks) {
		System.out.println("creating block name:" + name);
		if ((!mongo.insert(name, blocks))) {
			logger.info("Namenode create CONFLICT");
			throw new WebApplicationException(Status.CONFLICT);
		} else {
			// Remember the blocks that were added as part of the blob
			for (String block : blocks) {
				registeredBlocks.add(block);
			}
		}
	}

	@Override
	public synchronized void delete(String prefix) {
		List<String> removedBlocks = new ArrayList <> (); 
		removedBlocks.addAll(list(prefix));
		mongo.remove(prefix);
		if (!removedBlocks.isEmpty()) {
			for (String block : removedBlocks) {
				registeredBlocks.remove(block);
			}
		} else {
			logger.info("Namenode delete NOT FOUND");
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	@Override
	public synchronized void update(String name, List<String> blocks) {
		List<String> oldBlocks = new ArrayList <> (); 
		oldBlocks.addAll(mongo.update(name, blocks));
		if (!oldBlocks.isEmpty()) {
			// Blocks that were removed as part of this update are forgotten
			for (String block : oldBlocks) {
				if (!blocks.contains(block)) {
					registeredBlocks.remove(block);
				}
			}
			// New blocks that were added as part of this update are registered
			for (String block : blocks) {
				if (!oldBlocks.contains(block)) {
					registeredBlocks.add(block);
				}
			}
		} else {
			logger.info("Namenode update NOT FOUND");
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	@Override
	public synchronized List<String> read(String name) {
		List<String> mongoBlocks = new ArrayList <> (); 
		mongoBlocks.addAll(mongo.read(name));
		if (mongoBlocks == null || mongoBlocks.isEmpty()) {
			logger.info("Namenode read NOT FOUND");
			throw new WebApplicationException(Status.NOT_FOUND);
		} else
			logger.info("Blocks for Blob: " + name + " : " + mongoBlocks);
		return mongoBlocks;
	}

	public static void main(String[] args) throws UnknownHostException, URISyntaxException, NoSuchAlgorithmException, InterruptedException {
		System.setProperty("java.net.preferIPv4Stack", "true");
		String port = NAMENODE_PORT_DEFAULT;
		if (args.length > 0 && args[0] != null) {
			port = args[0];
		}
		String URI_BASE = "https://0.0.0.0:" + port + "/";
		String myAddress = "https://" + IP.hostAddress() + ":" + port;
		ResourceConfig config = new ResourceConfig();
		config.register(new NamenodeRest());
		JdkHttpServerFactory.createHttpServer(URI.create(URI_BASE), config, SSLContext.getDefault());

		System.err.println("Namenode ready....");
		if (RemoteBlobStorage.getKafka()) {
			System.err.println("Namenode" +RemoteBlobStorage.getKafka() );
			new Publisher("Discovery", "Namenode", myAddress + "/").start();
		} else {
			ServiceDiscovery.multicastReceive(ServiceDiscovery.NAMENODE_SERVICE_NAME, myAddress + "/");
		}
	}
}