package api.storage;

import java.util.List;
import java.util.Map;

public interface BlobStorage {

	List<String> listBlobs( String prefix );
	
	void deleteBlobs( String prefix );
	
	BlobReader readBlob( String name  );
	
	BlobWriter blobWriter( String name );
		
	interface BlobReader extends Iterable<String> {
		String readLine();
	}
	
	interface BlobWriter {
		void writeLine(String line);
		void close();
	}
	static boolean getKafka() {
		return false;
	}

	Map<String, Datanode> getDatanodeClients();

	Map<String, Namenode> getNamenodeClient();
}
