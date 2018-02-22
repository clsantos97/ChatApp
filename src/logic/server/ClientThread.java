package logic.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import logic.cfg.ChatAppConstants;
import model.Message;

public class ClientThread extends Thread {

	private Socket socket;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;
        
	private JTextArea textArea;
	private DefaultListModel<String> model;

	public ClientThread(Socket socket, JTextArea textArea, ObjectOutputStream ouput, ObjectInputStream input,
			DefaultListModel<String> model) throws IOException, ClassNotFoundException {
		this.socket = socket;
		this.textArea = textArea;
		this.output = ouput;
		this.input = input;
		this.model = model;

		Message message = (Message) input.readObject();
		textArea.append(message.toString());
		reSendAll(message);
		ServerThread.clients.put(message.getNickname(), this);
		setName(message.getNickname());
		start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				Message message = (Message) input.readObject();
				if (message.getMsg().equals(ChatAppConstants.DISCONNECTED)) {
					removeClient(message);
					break;
				} 
				reSendAll(message);
				textArea.append(message.toString());
			}
		} catch (SocketException e) {
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	
	public void removeClient(Message message) {
		textArea.append(message.toString());
		ServerThread.clients.remove(getName());
		model.removeElement(getName());
		reSendAll(message);
	}

	public void sendMessage(Message message) {
		try {
			output.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reSendAll(Message message) {
		for (ClientThread client : ServerThread.clients.values()) {
			client.sendMessage(message);
		}
	}

	public void disconnect() {
		try {
			if (output != null)
				output.close();
			if (input != null)
				input.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
