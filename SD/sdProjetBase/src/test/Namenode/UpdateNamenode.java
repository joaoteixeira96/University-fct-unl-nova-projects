package test.Namenode;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.*;
import javax.ws.rs.client.*;
import org.glassfish.jersey.client.*;


public class UpdateNamenode {
	public static void main(String[]args){
	ClientConfig config = new ClientConfig();
	Client client = ClientBuilder.newClient(config);

	URI baseURI = UriBuilder.fromUri("http://localhost:9990/").build();
	WebTarget target = client.target( baseURI );
	
	List<String> content = new ArrayList<String>();
	content.add("a3");    
	String prefix = "ola";

	Response response = target.path("/namenode/" + prefix)
	    .request()
	    .put( Entity.entity( content, MediaType.APPLICATION_JSON));
	        
	if( response.hasEntity() ) {
	    String id = response.readEntity(String.class);
	    System.out.println( "data resource id: " + id );
	} else
	    System.err.println( response.getStatus() );
}
}

