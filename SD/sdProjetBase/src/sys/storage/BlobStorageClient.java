package sys.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import library.LibMultCast;
import server.DatanodeServer;
import api.storage.BlobStorage;
import api.storage.Datanode;
import api.storage.Namenode;
import sys.storage.io.BufferedBlobReader;
import sys.storage.io.BufferedBlobWriter;

public class BlobStorageClient implements BlobStorage {

	private static final int BLOCK_SIZE = 1024;
	private static final String DATANODE = "Datanode";
	private static final String NAMENODE = "Namenode";
	//private static final int GC_TIMEOUT = 30000;

	private List<String> url_List = new ArrayList<String>();
	
	Namenode namenode;
	Datanode[] datanodes;
	
	public BlobStorageClient() {
		try {
			url_List = (LibMultCast.clientRequest(DATANODE));
			this.namenode = new NamenodeRest(LibMultCast.clientRequest(NAMENODE).get(0));
			datanodes = new Datanode [url_List.size()];
			buildDatanode();
		} catch (IOException e) {
			e.printStackTrace();
		}
/*
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						garbageCollection();
						Thread.sleep(GC_TIMEOUT);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t1.start();*/
		
	}

	public void buildDatanode() {
		int i = 0;
		for (String uri : url_List ) {
			this.datanodes[i++] = new DatanodeRest(uri);
		}
	}

	@Override
	public synchronized List<String> listBlobs(String prefix)  {
		return this.namenode.list(prefix);
	}

	@Override
	public synchronized void deleteBlobs(String prefix) {
		namenode.list(prefix).forEach(blob -> {
				namenode.read(blob).forEach(block -> {
						datanodes[0].deleteBlock(block);
				});
		});
		namenode.delete(prefix);
	}
	@Override
	public synchronized BlobReader readBlob(String name) {
		return new BufferedBlobReader(name, namenode, datanodes[0]);
	}
	@Override
	public synchronized BlobWriter blobWriter(String name) {
		return new BufferedBlobWriter(name, namenode, datanodes, BLOCK_SIZE);
	}
	/*public void garbageCollection() {
		System.out.println("in garbage collection ");
		for (Datanode d : datanodes) {
			d.garbageCollection(namenode.getBlocks());
		}
	}*/
}
