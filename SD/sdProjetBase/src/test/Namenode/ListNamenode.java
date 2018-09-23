package test.Namenode;

import java.net.URI;
import javax.ws.rs.core.*;
import javax.ws.rs.client.*;
import org.glassfish.jersey.client.*;

public class ListNamenode {
	public static void main(String[] args) {
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);

		URI baseURI = UriBuilder.fromUri("http://localhost:9990/").build();
		WebTarget target = client.target(baseURI);
		String prefix = "ol";

		Response response = target.path("/namenode/list/").queryParam("prefix", prefix).request().get();

		if (response.hasEntity()) {
			String id = response.readEntity(String.class);
			System.out.println("data resource id: " + id);
		} else
			System.err.println(response.getStatus());
	}
}
