package mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


import org.bson.Document;
import static com.mongodb.client.model.Filters.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

public class Mongo {
	private static final String WILDCARD = ".*";
	private static final String COLLECTION = "collection";
	private MongoClientURI uri;
	private MongoClient mongo;
	private MongoDatabase db;
	private static Logger logger = Logger.getLogger(Mongo.class.getName());

	public Mongo() {
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "OFF");
		uri = new MongoClientURI("mongodb://mongo1,mongo2,mongo3/?w=majority&readConcernLevel=majority&readPreference=primary");
		mongo = new MongoClient(uri);
		db = mongo.getDatabase("testDB");

	}

	public synchronized boolean insert(String name, List<String> blocks) {
		MongoCollection<Document> table = db.getCollection(COLLECTION);

		Document document = new Document();
		document.put("_id", name);
		document.put("name", name);
		document.put("blocks", blocks);
		Document d = table.find(document).first();
		try {
		if (d==null) {
			logger.info("create document:" + " name: "+ name +" blocks: " + blocks);
			table.insertOne(document);
			return true;
		}
		}catch (Exception e) {
			throw new WebApplicationException(Status.CONFLICT);
		}
		logger.info("check document:" + " name: "+ d.get("name", String.class)+" blocks: " + d.get("blocks", ArrayList.class));
		return false;

	}

	public synchronized List<String> list(String prefix) {
		List<String> list = new ArrayList<>();
		MongoCollection<Document> table = db.getCollection(COLLECTION);
		FindIterable<Document> iterable = table.find(regex("name", "^" + prefix + WILDCARD));
		for(Document d: iterable) {
			list.add(d.get("name", String.class));
		}
		System.out.println("list:" + list);
		return list;
	}

	public synchronized void remove(String prefix) {
		MongoCollection<Document> table = db.getCollection(COLLECTION);

		FindIterable<Document> iterable = table.find(regex("name", "^" + prefix + WILDCARD));
		for(Document d: iterable) {
			logger.info("removing document:" + " name: "+ d.get("name", String.class)+" blocks: " + d.get("blocks", ArrayList.class));
			table.deleteOne(d);
		}
	}

	public synchronized List<String> update(String name, List<String> blocks) {

		MongoCollection<Document> table = db.getCollection(COLLECTION);
		List<String> oldBlocks = read(name);

		Document newDoc = new Document();
		newDoc.put("name", name);
		newDoc.put("blocks", blocks);
		Document d = table.find(newDoc).first();
		
			if (d != null) {
			logger.info("update document:" + " name: "+ d.get("name", String.class)+" blocks: " + d.get("blocks", ArrayList.class));
			table.updateOne(eq("name", name), newDoc);
			}
		return oldBlocks;
	}

	@SuppressWarnings("unchecked")
	public synchronized List<String> read(String name) {
		MongoCollection<Document> table = db.getCollection(COLLECTION);
		Document d = table.find(eq("name", name)).first();
		logger.info("read document:" + " name: "+ d.get("name", String.class)+" blocks: " + d.get("blocks", ArrayList.class));
		return d.get("blocks", ArrayList.class);
	}
}