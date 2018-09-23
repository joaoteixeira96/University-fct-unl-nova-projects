package sys.storage;

import java.net.URI;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import api.storage.Datanode;

public class DatanodeRest implements Datanode {

	private static final String NO_CONTENT = "NO CONTENT";
	private static final String OK = "OK";
	private static final String DATANODE = "datanode";
	private static final String GC_PATH = "/gc/";
	final int CONNECT_TIMEOUT = 2000;
	final int READ_TIMEOUT = 2000;
	final int NUM_TRIES = 5, SLEEP_TIME = 1000;
	public String uri_base;
	private static Client client;
	private static ClientConfig config;

	public DatanodeRest(String URI_BASE) {
		uri_base = URI_BASE.concat(DATANODE);
		configClient();
	}

	private void configClient() {
		config = new ClientConfig();
		config.property(ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
		config.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);

		client = ClientBuilder.newClient(config);
	}

	public String createBlock(byte[] data) {
		String id = null;
		URI baseURI = UriBuilder.fromUri(uri_base).build();
		WebTarget target = client.target(baseURI);

		for (int i = 0; i < NUM_TRIES; i++) {
			try {
				Response response = target.request().post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
				if (response.hasEntity()) {
					id = response.readEntity(String.class);
					System.out.println("data resource id: " + id);
					return id;
				} else
					System.err.println(response.getStatus());

				Thread.sleep(SLEEP_TIME);
			} catch (Exception e) {
				configClient();
			}
		}
		return id;
	}

	@Override
	public void deleteBlock(String uri_block) {

		URI baseURI = UriBuilder.fromUri(uri_block).build();
		WebTarget target = client.target(baseURI);

		for (int i = 0; i < NUM_TRIES; i++) {
			try {
				Response response = target.request().delete();
				if (response.getStatus() >= 200 && response.getStatus() <= 299) {
					byte[] data = response.readEntity(byte[].class);
					System.out.println("data resource length: " + data.length);
					return;
				} else
					System.err.println(response.getStatus());
				Thread.sleep(SLEEP_TIME);
			} catch (Exception e) {
				configClient();
			}
		}
	}

	@Override
	public byte[] readBlock(String uri_block) {
		byte[] data = null;

		URI baseURI = UriBuilder.fromUri(uri_block).build();
		WebTarget target = client.target(baseURI);
		for (int i = 0; i < NUM_TRIES; i++) {
			try {
				Response response = target.request().get();

				if (response.hasEntity()) {
					data = response.readEntity(byte[].class);
					return data;
				} else
					System.err.println(response.getStatus());
				Thread.sleep(SLEEP_TIME);
			} catch (Exception e) {
				configClient();
			}
		}
		return data;
	}

	@Override
	public void garbageCollection(List<String> allBlocks) {
		URI baseURI = UriBuilder.fromUri(uri_base).build();
		WebTarget target = client.target(baseURI);
			for (int i = 0; i < NUM_TRIES; i++) {
				try {
					Response response = target.path(GC_PATH).request()
							.post(Entity.entity(allBlocks, MediaType.APPLICATION_JSON));
					if (response.getStatus() == 200) {
						System.out.println("Garbage collection was performed.");
						return;
					} else
						System.err.println(response.getStatus());
					Thread.sleep(SLEEP_TIME);
			}catch (Exception e) {
				configClient();
			}
		} 
	}
}
