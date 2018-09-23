package sys.storage;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import api.storage.BlobStorage;
import api.storage.Datanode;
import api.storage.Namenode;
import kafka.Subscriber;
import sys.storage.io.BufferedBlobReader;
import sys.storage.io.BufferedBlobWriter;
import utils.MapReduceCommon;
import utils.ServiceDiscovery;

public class RemoteBlobStorage implements BlobStorage {
	private static final int BLOCK_SIZE = 512;
	private Random gen = new Random();

	private static Logger logger = Logger.getLogger(RemoteBlobStorage.class.getName());

	Map<String, Namenode> namenodes;
	Map<String, Datanode> datanodes;

	public RemoteBlobStorage() throws NoSuchAlgorithmException, InterruptedException {
		
		datanodes = new ConcurrentHashMap<String, Datanode>();
		namenodes = new ConcurrentHashMap<String, Namenode>();
		
		if (getKafka()) {
		new Subscriber(namenodes, datanodes);
		for(String s : namenodes.keySet())
		System.err.println("namenode"+s);
		for(String s : datanodes.keySet())
		System.err.println("datanodes"+s);
		
		} else {
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
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
							// nothing to be done
						}
					}
				}
			};
			dataNodeDiscovery.start();
			
			Thread nameNodeDiscovery = new Thread() {
				public void run() {
					while (true) {
						String[] namenodeNames = ServiceDiscovery.multicastSend(ServiceDiscovery.NAMENODE_SERVICE_NAME);
						if (namenodeNames != null) {
							for (String namenode : namenodeNames) {
								if (!namenodes.containsKey(namenode)) {
									logger.info("New Namenode discovered: " + namenode);
									try {
										namenodes.put(namenode, new NamenodeClient(namenode));
									} catch (NoSuchAlgorithmException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
							// nothing to be done
						}
					}
				}
			};
			nameNodeDiscovery .start();
		}
	}
	//mudar tambem o seu valor na api do blobstorage
	public static boolean getKafka() {
		return false;
	}
	@Override
	public List<String> listBlobs(String prefix) {
		Namenode namenode = namenodes.get(namenodes.keySet().toArray()[gen.nextInt(namenodes.size())]);
		return namenode.list(prefix);
	}

	@Override
	public void deleteBlobs(String prefix) {
		Namenode namenode = namenodes.get(namenodes.keySet().toArray()[gen.nextInt(namenodes.size())]);
		namenode.list(prefix).forEach(blob -> {
			namenode.read(blob).forEach(block -> {
				String[] components = MapReduceCommon.getAddressFromBlockUUID(block);
				if (components != null && datanodes.containsKey(components[0])) {
					datanodes.get(components[0]).deleteBlock(components[1]);
				}
			});
		});
		namenode.delete(prefix);
	}

	@Override
	public BlobReader readBlob(String name) {
		logger.info("readBlob(" + name + ")");
		for (String addr : datanodes.keySet()) {
			logger.info("Datanode: " + addr);
		}
		return new BufferedBlobReader(name, namenodes.get(namenodes.keySet().toArray()[gen.nextInt(namenodes.size())]), datanodes);
	}

	@Override
	public BlobWriter blobWriter(String name) {
		Namenode namenode = namenodes.get(namenodes.keySet().toArray()[gen.nextInt(namenodes.size())]);
		return new BufferedBlobWriter(name, namenode, datanodes, BLOCK_SIZE);
	}

	@Override
	public Map<String, Datanode> getDatanodeClients() {
		return datanodes;
	}

	@Override
	public Map<String, Namenode>  getNamenodeClient() {
		return namenodes;
	}
}
