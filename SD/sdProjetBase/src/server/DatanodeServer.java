package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;

import api.storage.Datanode;
import utils.IP;

public class DatanodeServer implements Datanode {

	private static final String NO_CONTENT = "NO CONTENT";
	private static final String NOT_FOUND = "NOT FOUND";
	private static final String OK = "OK";
	private static final String SLASH = "/";
	private static final String DATANODE = "datanode";
	private static final String DATABASE = "database";

	private static Logger logger = Logger.getLogger(DatanodeServer.class.toString());
	public static String uri_base;

	public DatanodeServer(String URI_BASE) {
		uri_base = URI_BASE.concat(DATANODE);

	}

	public String createBlock(byte[] data) {
		String fileName = utils.Random.key64();
		File directory = new File(DATABASE);
		String uri_block = uri_base + SLASH + fileName;

		if (!directory.exists()) {
			directory.mkdir();
		}

		File file = new File(DATABASE + SLASH + fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return uri_block;
		}

		return uri_block;
	}

	@Override
	public void deleteBlock(String block) {
		File f = new File(DATABASE + SLASH + block);
		if (!f.exists()) {
			logger.info(NOT_FOUND);
			throw new WebApplicationException(Status.NOT_FOUND);
		} else {
			f.delete();
			logger.info(NO_CONTENT);
		}
	}

	@Override
	public byte[] readBlock(String block) {
		File f = new File(DATABASE + SLASH + block);
		byte[] data = new byte[(int) f.length()];

		try {
			FileInputStream fis = new FileInputStream(f);
			fis.read(data);
			fis.close();
		} catch (FileNotFoundException e) {
			logger.info(NOT_FOUND);
			throw new WebApplicationException(Status.NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (data != null)
			logger.info(OK);
		return data;
	}

	//falta considerar caso em que este metodo e chamado enquanto estao a ser
	//adicionados blocos e ainda nao houve tempo se os associar a um namenode
	public void garbageCollection(List<String> allBlocks) {
		File directory = new File(DATABASE);

		String[] files = directory.list();

		for (String f : files) {
			if (!allBlocks.contains(f))
				this.deleteBlock(f);
		}
		throw new WebApplicationException(Status.OK);
	}
}
