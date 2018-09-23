package test.Datanode;

import java.net.URI;
import javax.ws.rs.core.*;
import javax.ws.rs.client.*;
import org.glassfish.jersey.client.*;

import utils.IP;

public class DeleteResource {

	public static void main(String[]args){
		
ClientConfig config = new ClientConfig();
Client client = ClientBuilder.newClient(config);

URI baseURI = UriBuilder.fromUri("http://"+IP.hostAddress()+":9999/").build();
WebTarget target = client.target( baseURI );
        
Response response = target.path("/datanode/hvvgvch.txt")
    .request()
    .delete();
        
if( response.getStatus() >= 200 && response.getStatus() <= 299 ) {
    byte[] data = response.readEntity(byte[].class);
    System.out.println( "data resource length: " + data.length );
} else
    System.err.println( response.getStatus() );
}
}