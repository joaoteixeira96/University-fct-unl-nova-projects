package test.Datanode;

import java.net.URI;
import javax.ws.rs.core.*;
import javax.ws.rs.client.*;
import org.glassfish.jersey.client.*;

import utils.IP;


public class CreateResource {
	public static void main(String[]args){
	ClientConfig config = new ClientConfig();
	Client client = ClientBuilder.newClient(config);

	URI baseURI = UriBuilder.fromUri("http://"+IP.hostAddress()+":9999").build();
	WebTarget target = client.target( baseURI );
	        
	byte[] data = new byte[1024];
	data = "[B@51399530".getBytes();
	
	Response response = target.path("/datanode/")
	    .request()
	    .post( Entity.entity( data, MediaType.APPLICATION_OCTET_STREAM));
	        
	if( response.hasEntity() ) {
	    String id = response.readEntity(String.class);
	    System.out.println( "data resource id: " + id );
	} else
	    System.err.println( response.getStatus() );
}
}
