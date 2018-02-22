package logic.server;

import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

public class Manager {

	private ServerThread server;

	public void start(JTextArea textArea, DefaultListModel<String> model) {
		server = new ServerThread(textArea, model);
	}


	public void disconnect() throws IOException {
		server.disconnect();
	}

	public void kick(String user) {
		server.kickUser(user);
	}
}
