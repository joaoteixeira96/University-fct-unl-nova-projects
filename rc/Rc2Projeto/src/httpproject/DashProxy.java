package httpproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;

import httpproject.Descriptor;

public class DashProxy {

	private static final String SCAPE_SPLITTER = " ";
	private static final String DOTS_SPLITTER = ": ";
	private static final String GET_SLASH = "GET /";
	private static final String GET_SPACE = "GET ";
	private static final String HTTP1_0_CRLF_CRLF = " HTTP/1.0\r\n\r\n";
	private static final String HTTP1_0_CRLF = " HTTP/1.0\r\n";
	private static String currentMovie;
	private static String currentPlayerId;
	private static Boolean start = false;
	private static Boolean init = true;
	private static Boolean noContentLeft = true;
	private static InetAddress dockerAdress;
	private static int dockerPort;
	private static int playoutDelay;
	private static int currentSegment = 1;
	private static int currentQuality = 1;
	private static int currentBandWidth = 0;
	private static HashMap<String, Descriptor> descriptors;

	// le o pedido do browser
	
	public static void handleBrowserRequest(InputStream in) throws IOException {
		
		String request;
		while((request = HTTPUtilities.readLine(in)).isEmpty());
		
		String[] requestParts = request.split("/");

		if (requestParts[0].equals(GET_SPACE) && requestParts[3].equals("start HTTP")) {
			currentPlayerId = requestParts[1];
			currentMovie = requestParts[2];
			start = true;
		} else if (start && requestParts[0].equals(GET_SPACE) && requestParts[3].equals("next-segment")) {
			if (currentPlayerId.equals(requestParts[1]) && currentMovie.equals(requestParts[2])) {
				getNextSegment();
			}
	
		} else
			System.err.println("GET required");
	}
	
	public static StringBuilder makeHeader() {
		
		StringBuilder reply = new StringBuilder("HTTP/1.0 200 OK\r\n");
		if(init) {
			int soma = 1237 + getTrack(currentQuality-1).getSegmentByIndex(currentSegment).getsize() ;
			
			reply.append("Content-Length: " + soma + "\r\n");
			reply.append("Content-Type: "+ getTrack(currentQuality-1).getContentType() +"\r\n");
			reply.append("Access-Control-Allow-Origin: *"+"\r\n\r\n");
		}
		else {
					
			reply.append("Content-Length: " + getTrack(currentQuality-1).getSegmentByIndex(currentSegment).getsize() + "\r\n");
			reply.append("Access-Control-Allow-Origin: *"+"\r\n\r\n");
		}
		return reply;
	}
	//manda reply para o browser
	public static void makeBrowserReply(OutputStream out) throws IOException {
		
		String header = makeHeader().toString();
		System.out.println(header);
		out.write(header.getBytes());
		
		if(init) {
			//enviar o init que é o for
			byte[] initBuff = handleDockerReply();
			System.out.println(initBuff);
			int n = initBuff.length;
			for (int i = 0; i<= initBuff.length ; i+=1024) {
				if(n>1024) {
					out.write(initBuff, i, 1024);
					n-=1024;
				}
				else {
					out.write(initBuff, i, n);
					n=0;
				}
			}
		}
			
		byte[] segBuff = handleDockerReply();
		int toRead = segBuff.length;
		for (int i = 0; i<= segBuff.length ; i+=1024) {
			if(toRead>1024) {
				out.write(segBuff, i, 1024);
				toRead-=1024;
			}
			else {
				out.write(segBuff, i, toRead);
				toRead=0;
			}
		}
		
	}

	public static void getNextSegment() {
		calculateQuality();
		if (getTrack(currentQuality-1).getSegmentByIndex(currentSegment + 1) != null) {
			currentSegment++;
		}

	}

	public static int calculateQuality() {
		int movieTracks = descriptors.get(currentMovie).getMovieTracks();

		for (int i = 0; i < movieTracks; i++) {
			if (currentBandWidth >= getTrack(i).getAvgBandwidth()) {
				currentQuality = getTrack(i).getVideoTracker();
				init = true;
			} else
				init = false;
		}

		return currentQuality;
	}

	public static Track getTrack(int index) {
		return descriptors.get(currentMovie).getTrackByIndex(index);
	}

	// Pede init ou videoLink ao docker
	public static void makeRequestDocker() throws IOException {
		// envia init se pode variar a qualidade do filme
		if (init) {
			Socket s1 = new Socket(dockerAdress, dockerPort);

			OutputStream out1 = s1.getOutputStream();
			
			String toWriteInit = (GET_SLASH + currentMovie + "/video/" + currentQuality + "/init.mp4" + 
					HTTP1_0_CRLF + "User-Agent: X-RC2017\r\n\r\n");
			
			System.out.println(toWriteInit);
			out1.write(toWriteInit.getBytes());
			s1.close();
			System.out.println("make1");
		} 

		Socket s2 = new Socket(dockerAdress, dockerPort);

		OutputStream out2 = s2.getOutputStream();
		String toWrite = GET_SLASH + currentMovie + "/" 
				+ getTrack(currentQuality-1).getSegmentByIndex(currentSegment).getlink() 
				+ HTTP1_0_CRLF +"User-Agent: X-RC2017\r\n\r\n";
		System.out.println(toWrite);

		out2.write(toWrite.getBytes());
		s2.close();
		System.out.println("make2");
	}

	// le filmes que existem no docker
	private static List<String> dockerMovies() throws IOException {
		List<String> movies = new LinkedList<>();

		Socket s = new Socket(dockerAdress, dockerPort);

		InputStream in = s.getInputStream();
		OutputStream out = s.getOutputStream();

		out.write((GET_SLASH + "movies.txt" + HTTP1_0_CRLF + "User-Agent: X-RC2017\r\n\r\n").getBytes());
		Scanner scanner = new Scanner(in);

		while (scanner.hasNextLine() && !(scanner.nextLine()).isEmpty());

		while (scanner.hasNext()) {
			movies.add(scanner.nextLine());
		}
		scanner.close();
		s.close();
		return movies;
	}
	
	

	// cria o descritor
	public static void prepareDescriptor(List<String> movies) throws IOException {

		
		
		for (String movie : movies) {

			Socket s = new Socket(dockerAdress, dockerPort);

			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();

			out.write((GET_SLASH + movie + "/descriptor.txt" + HTTP1_0_CRLF + "User-Agent: X-RC2017\r\n\r\n").getBytes());
				
			String line = HTTPUtilities.readLine(in);
			
			while(!line.equals("")) {
				System.out.println(line);
				line = HTTPUtilities.readLine(in);
			}
			
//			while(line.trim().equals(""));
//			line = HTTPUtilities.readLine(in);
			
			int movieTracks = Integer.parseInt(HTTPUtilities.readLine(in).split(DOTS_SPLITTER)[1]);
			int segmentDuration = Integer.parseInt(HTTPUtilities.readLine(in).split(DOTS_SPLITTER)[1]);
			
			Descriptor descriptor = new Descriptor(movieTracks, segmentDuration);
			
			int trackNum = 1;
			while(trackNum != movieTracks) {
				
				trackNum = Integer.parseInt(HTTPUtilities.readLine(in).split(" ")[1]);
				String content = HTTPUtilities.readLine(in).split(" ")[1].replaceAll(";", "");
				int bandWidthNum = Integer.parseInt(HTTPUtilities.readLine(in).split(" ")[1]);
				
				Track track = new Track(trackNum, content, bandWidthNum);
				
				String segment = "";
				
				while(!(line = HTTPUtilities.readLine(in)).equals("")) {
					
					segment = line;
					System.out.println(line);
					
					String video = segment.split(" ")[0];
					int videoSize = Integer.parseInt(segment.split(" ")[1]);
				
					Segment newSegment = new Segment(video,videoSize);
					track.addSegment(newSegment);
				}
				descriptor.addTrack(track);
				System.out.println();
			}
			descriptors.put(movie, descriptor);
			s.close();
		}
	}
	//recebe reply do docker
	public static byte[] handleDockerReply() throws IOException {

		Socket s = new Socket(dockerAdress, dockerPort);
		
		int contentLenght = -1;

		InputStream in = s.getInputStream();
		System.out.println("aqui");
		System.out.println(HTTPUtilities.readLine(in));
		
		if(HTTPUtilities.readLine(in).split(SCAPE_SPLITTER)[1].equals("200") && HTTPUtilities.readLine(in).split(SCAPE_SPLITTER)[2].equals("OK")) {

			while(!HTTPUtilities.readLine(in).split(DOTS_SPLITTER)[0].equals("Content-Lenght"));
			
			contentLenght = Integer.parseInt(HTTPUtilities.readLine(in).split(DOTS_SPLITTER)[1]);
			
			while(!HTTPUtilities.readLine(in).equals(""));
			
			if(contentLenght == 0) {
				noContentLeft = true;
				
			}
			
			byte[] buffer = new byte[contentLenght];
			
			int i = 0;
			while(i < contentLenght) {
				i += in.read(buffer,i,contentLenght-i);
			}
			s.close();
			return buffer;
		}
		s.close();
		return null;
	}
	// Copies data from an input stream to an output stream
	static void dumpStream(InputStream in, OutputStream out) throws IOException {
		byte[] arr = new byte[1024];
		for (;;) {
			int n = in.read(arr);
			if (n == -1)
				break;
			out.write(arr, 0, n);
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("usage: java DashProxy URL-Base playout-delay");
			System.exit(0);
		}
		
		descriptors = new HashMap<String, Descriptor>();
		currentQuality = 1;
		String url = args[0];// http://localhost:8080
		URL u = new URL(url);
		
		dockerAdress = InetAddress.getByName(u.getHost());
		dockerPort = u.getPort();
		
		System.out.println(dockerAdress);
		System.out.println(dockerPort);
		
		playoutDelay = Integer.parseInt(args[1]);
		//dockerPort = Integer.parseInt(HTTPUtilities.getPortURL(url));
		//dockerAdress = InetAddress.getByName("localhost");
		prepareDescriptor(dockerMovies());
		
		for(int i = 0; i< 50 ; i++)
		System.out.println(descriptors.get("coco").getTrackByIndex(currentQuality-1).getSegmentByIndex(i).getlink());
		
		for (;;) {
			System.out.println("------------------");
			
			ServerSocket ss = new ServerSocket(1234);
			Socket clientS = ss.accept();
			InputStream in = clientS.getInputStream();
			OutputStream out = clientS.getOutputStream();
			
			System.out.println("START");
			handleBrowserRequest(in);
			System.out.println("FEZ HANDLE REQUEST");
			makeRequestDocker();
			System.out.println("FEZ MAKE REQUEST");
			
			makeBrowserReply(out);
			System.out.println("FEZ MAKE REPLY");
			
			clientS.close();
			ss.close();
		}
	
	}
}
