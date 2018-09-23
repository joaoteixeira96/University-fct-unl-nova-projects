package server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

import api.storage.Namenode;

/*
 * 
 * Rather than invoking the Namenode via REST, executes
 * operations locally, in memory.
 * 
 * Uses a trie to perform efficient prefix query operations.
 */
public class NamenodeServer implements Namenode {

	private static final String NOT_FOUND = "NOT FOUND";

	private static final String WAS_CREATED = " was created.";

	private static final String NAMENODE2 = "Namenode ";

	private static final String NO_CONTENT = "NO CONTENT";

	private static final String CONFLICT = "CONFLICT";

	private static final String OK = "OK";

	private static final String NAMENODE = "namenode";

	private static Logger logger = Logger.getLogger(NamenodeServer.class.toString());

	private Trie<String, List<String>> names;
	public static String uri_base;

	public NamenodeServer(String URI_BASE) {
		names = new PatriciaTrie<>();
		uri_base = URI_BASE.concat(NAMENODE);
	}

	@Override
	public synchronized List<String> list(String prefix) {
		logger.info(OK);
		return new ArrayList<>(names.prefixMap(prefix).keySet());
	}

	// name - nome do blob
	@Override
	public synchronized void create(String name, List<String> blocks) {
		if (this.names.putIfAbsent(name, new ArrayList<>(blocks)) != null) {
			logger.info(CONFLICT);
			throw new WebApplicationException(Status.CONFLICT);
		} else {
			logger.info(NO_CONTENT);
		}

	}

	@Override
	public synchronized void delete(String prefix) {
		List<String> keys = new ArrayList<String>(this.names.prefixMap(prefix).keySet());
		if (this.names.prefixMap(prefix).isEmpty()) {
			logger.info(NOT_FOUND);
			throw new WebApplicationException(Status.NOT_FOUND);
		} else {
			this.names.keySet().removeAll(keys);
			logger.info(NO_CONTENT);
		}
	}

	@Override
	public synchronized void update(String name, List<String> blocks) {
		if (names.prefixMap(name).isEmpty()) {
			logger.info(NOT_FOUND);
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		names.prefixMap(name).remove(name);
		names.prefixMap(name).put(name, new ArrayList<>(blocks));
		logger.info(NO_CONTENT);

	}

	@Override
	public synchronized List<String> read(String name) {
		List<String> blocks = this.names.get(name);
		if (this.names.get(name).isEmpty()) {
			logger.info(NOT_FOUND);
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return blocks;
	}
	
	public List <String> getBlocks(){
		return names.get("");
	}
}
