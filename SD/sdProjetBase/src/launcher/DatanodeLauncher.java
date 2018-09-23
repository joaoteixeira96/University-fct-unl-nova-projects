package launcher;

import java.io.IOException;
import java.net.*;
import org.glassfish.jersey.server.*;
import utils.IP;
import library.LibMultCast;
import server.DatanodeServer;

import org.glassfish.jersey.jdkhttp.*;

public class DatanodeLauncher{
	public static void main(String[]args) throws IOException{
		
		String URI_BASE = "http://"+IP.hostAddress()+":9999/";
		String URI_DEFAULT = "http://0.0.0.0:9999/";
		

		ResourceConfig config = new ResourceConfig();
		config.register( new DatanodeServer(URI_BASE));

		JdkHttpServerFactory.createHttpServer( URI.create(URI_DEFAULT), config);
		System.err.println(URI_BASE);
		String receivemsg = "Datanode";
		System.err.println("Server ready....");
		LibMultCast.serverAnnounce(receivemsg,URI_BASE);
	
	}
}