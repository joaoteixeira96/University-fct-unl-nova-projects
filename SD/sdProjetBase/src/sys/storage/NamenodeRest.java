package sys.storage;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import api.storage.Namenode;

/*
 * 
 * Rather than invoking the Namenode via REST, executes
 * operations locally, in memory.
 * 
 * Uses a trie to perform efficient prefix query operations.
 */
public class NamenodeRest implements Namenode {

	private static final String NO_CONTENT = "NO CONTENT";
	final int CONNECT_TIMEOUT = 2000;
	final int READ_TIMEOUT = 2000;
	final int NUM_TRIES = 5, SLEEP_TIME = 1000;
	private static final String OK = "OK";
	private static final String GET_BLOCKS_PATH = "/listBlocks/";
	private static final String DELETE_PATH = "/list/";

	private static final String NAMENODE = "namenode/";

	public String uri_base;
	public static ClientConfig config;
	public static Client client;
	public WebTarget target;
	public URI baseURI;

	public NamenodeRest(String URI_BASE) {
		uri_base = URI_BASE.concat(NAMENODE);
		configClient();
	}

	private void configClient() {
		config = new ClientConfig();
		config.property(ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
		config.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);

		client = ClientBuilder.newClient(config);
		baseURI = UriBuilder.fromUri(uri_base).build();
		target = client.target(baseURI);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized List<String> list(String prefix) {
		List<String> list = null;

		for (int i = 0; i < NUM_TRIES; i++) {
			try {
				Response response = target.path("/list/").queryParam("prefix", prefix).request().get();
				list = response.readEntity(List.class);
				if (!list.isEmpty())
					return list;
				else
					System.err.println(response.getStatus());
				Thread.sleep(SLEEP_TIME);

			} catch (Exception e) {
				configClient();
			}
		}
		return list;
	}

	// name - nome do blob lista de uri
	@Override
	public synchronized void create(String name, List<String> blocks) {
		for (int i = 0; i < NUM_TRIES; i++) {
			try {
				Response response = target.path(name).request().post(Entity.entity(blocks, MediaType.APPLICATION_JSON));
				if (response.hasEntity()) {
					String id = response.readEntity(String.class);
					System.out.println("data resource id: " + id);
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
	public synchronized void delete(String prefix) {
		try {
			for (int i = 0; i < NUM_TRIES; i++) {
				Response response = target.path(DELETE_PATH).queryParam("prefix", prefix).request().delete();
				if (response.hasEntity()) {
					String id = response.readEntity(String.class);
					System.out.println("data resource id: " + id);
					return;
				} else
					System.err.println(response.getStatus());
				Thread.sleep(SLEEP_TIME);
			}
		} catch (Exception e) {
			configClient();
		}
	}

	@Override
	public synchronized void update(String name, List<String> blocks) {
		for (int i = 0; i < NUM_TRIES; i++) {
			try {
				Response response = target.path(name).request()
						.put(Entity.entity(new ArrayList<String>(blocks), MediaType.APPLICATION_JSON_TYPE));
				if (response.hasEntity()) {
					String id = response.readEntity(String.class);
					System.out.println("data resource id: " + id);
					return;
				} else
					System.err.println(response.getStatus());
				Thread.sleep(SLEEP_TIME);
			} catch (Exception e) {
				configClient();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized List<String> read(String name) {
		System.out.println("read blob: " + name);
		List<String> data = null;
		for (int i = 0; i < NUM_TRIES; i++) {
			try {
				Response response = target.path(name).request().get();
				if (response.hasEntity()) {
					data = response.readEntity(ArrayList.class);
					return data;
				} else {
					System.err.println(response.getStatus());
				}
				Thread.sleep(SLEEP_TIME);
			} catch (Exception e) {
				configClient();
			}
		}
		return data;
	}

	public List<String> getBlocks() {
		List<String> list = null;
		for (int i = 0; i < NUM_TRIES; i++) {
			try {
				Response response = target.path(GET_BLOCKS_PATH).request().get();
				if (response.hasEntity()) {
					list = response.readEntity(List.class);
					if (!list.isEmpty())
						return list;
				} else {
					System.err.println(response.getStatus());
				}
				Thread.sleep(SLEEP_TIME);
			} catch (Exception e) {
				configClient();
			}
		}
		return list;
	}
}
