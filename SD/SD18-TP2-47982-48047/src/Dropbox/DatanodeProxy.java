package Dropbox;

import java.net.URI;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.pac4j.scribe.builder.api.DropboxApi20;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import Dropbox.msgs.DeleteBlockArgs;
import Dropbox.msgs.ReadBlockArgs;
import Dropbox.msgs.UploadBlockArgs;
import api.storage.Datanode;
import kafka.Publisher;
import sys.storage.RemoteBlobStorage;
import utils.IP;
import utils.JSON;
import utils.ServiceDiscovery;

public class DatanodeProxy implements Datanode {

	private static final String UPLOAD_FILE_URL = "https://content.dropboxapi.com/2/files/upload";
	private static final String DELETE_FILE_URL = "https://api.dropboxapi.com/2/files/delete_v2";
	private static final String READ_FILE_URL = "https://content.dropboxapi.com/2/files/download";

	private static final String apiKey = "qss3305lcqrr1b3";
	private static final String apiSecret = "qpl0uojien2395e";

	protected static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
	protected static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";
	private static final String DATANODE_PORT_DEFAULT = "1234";
	private static Random gen = new Random();
	protected static String my_adress;

	protected OAuth20Service service;
	protected OAuth2AccessToken accessToken;

	protected DatanodeProxy() {

		try {
			service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret).build(DropboxApi20.INSTANCE);

			accessToken = new OAuth2AccessToken("hBxkRVk1j3AAAAAAAAAAXj7X5WchvnngPEPS4stCnM1tAut8NltGO_aO2wZrr0Kq");

		} catch (Exception x) {
			x.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public String createBlock(byte[] content) {
		try {
			String blockId = utils.Random.key64();
			OAuthRequest uploadFile = new OAuthRequest(Verb.POST, UPLOAD_FILE_URL);
			uploadFile.addHeader("Content-Type", OCTET_STREAM_CONTENT_TYPE);

			uploadFile.addHeader("Dropbox-API-Arg",
					JSON.encode(new UploadBlockArgs("/Datanode/" + blockId, "add", false, false)));

			uploadFile.setPayload(content);

			service.signRequest(accessToken, uploadFile);
			Response r = service.execute(uploadFile);

			if (r.getCode() == 200) {
				return my_adress + PATH + "/" + blockId;
			} else {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (Exception e) {
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public byte[] readBlock(String blockId) {
		try {
			OAuthRequest readFile = new OAuthRequest(Verb.POST, READ_FILE_URL);
			readFile.addHeader("Content-Type", OCTET_STREAM_CONTENT_TYPE);
			readFile.addHeader("Dropbox-API-Arg", JSON.encode(new ReadBlockArgs("/Datanode/" + blockId)));

			service.signRequest(accessToken, readFile);
			Response r = service.execute(readFile);

			if (r.getCode() == 200) {
				return r.getBody().getBytes();
			} else {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (Exception e) {
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void deleteBlock(String blockId) {
		try {
			OAuthRequest deleteFile = new OAuthRequest(Verb.POST, DELETE_FILE_URL);
			deleteFile.addHeader("Content-Type", JSON_CONTENT_TYPE);
			deleteFile.setPayload(JSON.encode(new DeleteBlockArgs("/Datanode/" + blockId)));

			service.signRequest(accessToken, deleteFile);
			Response r = service.execute(deleteFile);

			if (r.getCode() == 200) {
				return;
			} else {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (Exception e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");

		String port = DATANODE_PORT_DEFAULT;
		if (args.length > 0 && args[0] != null) {
			port = args[0];
		}
		String URI_BASE = "https://0.0.0.0:" + port + "/";
		ResourceConfig config = new ResourceConfig();
		my_adress = "https://" + IP.hostAddress() + ":" + port;
		config.register(new DatanodeProxy());
		JdkHttpServerFactory.createHttpServer(URI.create(URI_BASE), config, SSLContext.getDefault());

		System.err.println("Datanode Proxy ready....");
		if (RemoteBlobStorage.getKafka()) {
			System.err.println("Datanode Proxy " +RemoteBlobStorage.getKafka());
			new Publisher("Discovery", "Datanode", my_adress + "/").start();
		} else {
			ServiceDiscovery.multicastReceive(ServiceDiscovery.DATANODE_SERVICE_NAME, my_adress + "/");
		}
	}

}
