package logic.server;

import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import logic.cfg.ChatAppConstants;
import model.Message;

public class ServerThread extends Thread {

	public final static int PORT = 5100;
	private ServerSocket server = null;
	static ConcurrentSkipListMap<String, ClientThread> clients = new ConcurrentSkipListMap<>();
	private JTextArea textArea;
	private DefaultListModel<String> model;


	public ServerThread(JTextArea textArea, DefaultListModel<String> model) {
		this.textArea = textArea;
		this.model = model;
		start();
	}

	@Override
	@SuppressWarnings("unused")
	public void run() {
		// I/O streams.
		ObjectOutputStream output = null;
		ObjectInputStream input = null;
		try {
			// Create the server socket.
			server = new ServerSocket(PORT);

			// Loop to listen to incoming connections.
			while (true) {
				// Accept the connection.
				Socket socket = server.accept();

				// Initialize I/O streams to check incoming connection nickname.
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());

				// Get initial client message to check nickname.
				Message message = (Message) input.readObject();

				if (!ServerThread.clients.containsKey(message.getNickname())) {
					// If the nickname is valid, send approve message.
					output.writeObject(new Message(ChatAppConstants.SERVER_NAME, "1"));

					ClientThread client = new ClientThread(socket, textArea, output, input, model);
					List<String> clientKeys = new ArrayList<>(ServerThread.clients.keySet());
					int pos = clientKeys.indexOf(message.getNickname());
					model.add(pos, message.getNickname());
				} else {
					output.writeObject(new Message(ChatAppConstants.SERVER_NAME, "0"));
					socket.close();
				}
			}
		} catch (SocketException e) {
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (server != null)
					server.close();
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void disconnect() throws IOException {
		Message message = new Message(ChatAppConstants.SERVER_NAME, ChatAppConstants.MSG_DISCONNECT);
		for (ClientThread client : ServerThread.clients.values()) {
			client.sendMessage(message);
			client.disconnect();
		}
		if (server != null)
			server.close();
	}

	public void kickUser(String client) {
		ClientThread clientThread = ServerThread.clients.get(client);

		clientThread.sendMessage(new Message(ChatAppConstants.SERVER_NAME, ChatAppConstants.MSG_USER_KICKED));
		StringBuffer sb = new StringBuffer();
		sb.append(clientThread.getName());
		sb.append(ChatAppConstants.MSG_USER_KICKED);
		Message message = new Message(ChatAppConstants.SERVER_NAME, sb.toString());
		clientThread.removeClient(message);
		clientThread.disconnect();
	}
}
