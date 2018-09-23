package test.Datanode;

import java.net.URI;
import javax.ws.rs.core.*;
import javax.ws.rs.client.*;
import org.glassfish.jersey.client.*;

import utils.IP;

public class GetResource {

	public static void main(String[]args){
		
ClientConfig config = new ClientConfig();
Client client = ClientBuilder.newClient(config);

URI baseURI = UriBuilder.fromUri("http://"+IP.hostAddress()+":9999/").build();
WebTarget target = client.target( baseURI );
        
Response response = target.path("/datanode/gu0ajf5tn")
    .request()
    .get();
        
if( response.hasEntity() ) {
    byte[] data = response.readEntity(byte[].class);
    System.out.println( "data resource length: " + data.length );
    System.out.println( "data resource : " + data );
} else
    System.err.println( response.getStatus() );
}
}