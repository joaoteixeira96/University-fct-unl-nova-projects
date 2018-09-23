package sys.storage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import api.storage.Datanode;
import kafka.Publisher;
import sys.storage.BlockIO;
import utils.Base58;
import utils.IP;
import utils.ServiceDiscovery;

public class DatanodeRest implements Datanode {

	private static final String DATANODE_PORT_DEFAULT = "9999";

	private static Logger logger = Logger.getLogger(Datanode.class.getName());

	private String address;
	
	private static Random gen = new Random();

	public DatanodeRest(String myURL) {
		this.address = myURL;
		BlockIO.checkAndCreateDirectory(); // Check if the directory where blocks are going to be create exists, if not
											// creates.
	}

	@Override
	public synchronized String createBlock(byte[] data) {
		String blockId = null;
		while (true) {
			try {
				blockId = utils.Random.key64() + Base58.encode(utils.Hash.md5(data)); // Generates a random id for the block
				if (BlockIO.writeBlock(blockId, data)) // Stores the block in a file (false if the file already exists)
					break;
			} catch (IOException e) {
				logger.log(Level.WARNING, String.format("Error writting block to disk: %s.", blockId));
			}
		}

		return address + PATH + "/" + blockId; // Returns the full url to access the block

	}

	@Override
	public synchronized void deleteBlock(String block) {
		if (!BlockIO.deleteBlock(block)) { // deletes the block from disk (false if the corresponding file does not
											// exists)
			logger.log(Level.INFO, String.format("Couldn't find block %s", block));
			throw new WebApplicationException(Status.NOT_FOUND); // Sends a 404 if the block was not found in disk
		}
	}

	@Override
	public synchronized byte[] readBlock(String block) {
		logger.log(Level.FINE, String.format("Reading block with id %s.", block));
		byte[] data = BlockIO.readBlock(block); // Reads block from the disk (null if the file is not found)
		if (data == null) {
			logger.log(Level.INFO, String.format("Couldn't find block %s", block));
			throw new WebApplicationException(Status.NOT_FOUND); // Sends 404
		}
		String t = Base58.encode(utils.Hash.md5(data));
		if(!block.endsWith(t))
			return "<<<CORRUPTED BLOCK>>>\n".getBytes();
		
		return data;
	}

	public static void main(String[] args) throws UnknownHostException, URISyntaxException, NoSuchAlgorithmException {
		System.setProperty("java.net.preferIPv4Stack", "true");

		String port = DATANODE_PORT_DEFAULT;
		if (args.length > 0 && args[0] != null) {
			port = args[0];
		}
		String URI_BASE = "https://0.0.0.0:" + port + "/";
		ResourceConfig config = new ResourceConfig();
		String myAddress = "https://" + IP.hostAddress() + ":" + port;
		config.register(new DatanodeRest(myAddress));
		JdkHttpServerFactory.createHttpServer(URI.create(URI_BASE), config, SSLContext.getDefault());

		System.err.println("Datanode ready....");
		if (RemoteBlobStorage.getKafka()) {
			System.err.println("Datanode" +RemoteBlobStorage.getKafka() );
			new Publisher("Discovery", "Datanode", myAddress + "/").start();
		} else {
			ServiceDiscovery.multicastReceive(ServiceDiscovery.DATANODE_SERVICE_NAME, myAddress + "/");
		}
	}
}
