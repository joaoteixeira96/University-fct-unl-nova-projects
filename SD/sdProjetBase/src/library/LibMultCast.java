package library;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import utils.IP;

public class LibMultCast {

	final static int MAX_DATAGRAM_SIZE = 65536;
	final int MAX_DATAGRAM_SIZE_2 = 10;

	private static final long TIMEOUT = 10000;
	private static final int PORT = 9999;
	private static final int SOCKET_TIMEOUT = 500;
	private static final String GROUP_IP = "230.1.2.3";

	public static InetAddress group;

	public synchronized static void serverAnnounce(String receivemsg, String sendMsg) throws IOException {

		makeGroup();

		try (MulticastSocket socket = new MulticastSocket(PORT)) {
			socket.joinGroup(group);
			while (true) {

				byte[] buffer = new byte[MAX_DATAGRAM_SIZE];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);

				String msg = new String(request.getData(), 0, request.getLength());

				if (msg.equals(receivemsg)) {

					byte[] bufferPlayload = sendMsg.getBytes();
					DatagramPacket reply = new DatagramPacket(bufferPlayload, bufferPlayload.length);
					reply.setAddress(request.getAddress());
					reply.setPort(request.getPort());

					socket.send(reply);
				}
			}
		}
	}

	public synchronized static List<String> clientRequest(String NamenodeOrDatanode) throws IOException {

		makeGroup();
		byte[] data = NamenodeOrDatanode.getBytes();
		List<String> urlList = new ArrayList<String>();

		try (MulticastSocket socket = new MulticastSocket()) {

			socket.setSoTimeout(SOCKET_TIMEOUT);
			long time = System.currentTimeMillis();
			while (System.currentTimeMillis() <= time + TIMEOUT) {

				DatagramPacket request = new DatagramPacket(data, data.length, group, PORT);
				String msg = new String(request.getData(), 0, request.getLength());
				socket.send(request);

				try {
					while (true) {
						byte[] buffer = new byte[MAX_DATAGRAM_SIZE];

						DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
						socket.receive(reply);
						String reply_url = new String(reply.getData(), 0, reply.getLength());
						urlList.add(reply_url);

					}

				} catch (SocketTimeoutException p) {
					System.out.println(p.getStackTrace());
					return urlList;
				}
			}
		}
		return urlList;
	}

	public static void makeGroup() throws UnknownHostException {
		if (group == null) {
			group = InetAddress.getByName(GROUP_IP);

			if (!group.isMulticastAddress()) {
				System.out.println("Not a multicast address (use range : 224.0.0.0 -- 239.255.255.255)");
				System.exit(1);
			}
		}
	}
}
