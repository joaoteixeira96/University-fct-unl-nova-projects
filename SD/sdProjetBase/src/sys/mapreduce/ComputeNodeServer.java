package sys.mapreduce;


import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import api.mapreduce.ComputeNode;
import api.storage.BlobStorage;
import utils.IP;

@WebService(serviceName = ComputeNode.NAME, targetNamespace = ComputeNode.NAMESPACE, endpointInterface = ComputeNode.INTERFACE)
public class ComputeNodeServer implements ComputeNode {

	private static final int PORT = 6666;
	private static final String BASE_URI = "http://%s:%d" + ComputeNode.PATH;

	public static void main(String[] args) throws Exception {
		String baseURI = String.format(BASE_URI, IP.hostAddress(), PORT);
		Endpoint.publish(baseURI, new ComputeNodeServer());
		System.err.println("SOAP DatanodeServer Server ready...");
	}

	@Override
	public void mapReduce(String jobClassBlob, String inputPrefix, String outputPrefix, int outPartSize)
			throws ComputeNodeServer.InvalidArgumentException {
		if (jobClassBlob == null || inputPrefix == null || outputPrefix == null || outPartSize <= 0) {
			throw new InvalidArgumentException();
		}
		BlobStorage storage = new sys.storage.BlobStorageClient();
		MapReduceEngine engine = new MapReduceEngine("local", storage);

		engine.executeJob(jobClassBlob, inputPrefix, outputPrefix, outPartSize);
	}

}
