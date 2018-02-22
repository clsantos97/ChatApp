package logic.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JTextArea;
import model.Message;

public class Manager {

    public static final int PORT = 5100;
    public static final String HOST = "127.0.0.1";
    private Socket socket = null;
    ObjectOutputStream output = null;
    ObjectInputStream input = null;

    public void start(String nickName, JTextArea textArea, JButton btnSend) throws IOException {
        new ListenerThread(socket, nickName, textArea, btnSend, input);
    }

    public void sendMessage(Message message) throws SocketException, IOException {
        output.writeObject(message);
    }

    public void connect(String nickname)
            throws UnknownHostException, IOException, ClassNotFoundException, NicknameException {

        socket = new Socket(HOST, PORT);

        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        sendMessage(new Message(nickname, nickname+" conectado."));

        Message message = (Message) input.readObject();
        if (!message.getMsg().equals("1")) {
            socket.close();
            throw new NicknameException();
        }else{
            sendMessage(new Message(nickname, nickname+" conectado."));
        }
    }

    public void disconnect(String nickname) throws SocketException, IOException {
        // Send last "Bye message"
        sendMessage(new Message(nickname, nickname+" desconectado."));

        if (output != null) {
            output.close();
        }
        if (input != null) {
            input.close();
        }
        if (socket != null) {
            socket.close();
        }
    }

    public class NicknameException extends Exception {

        public NicknameException() {
            super("El nickname ya esta siendo utilizado");
        }
        public NicknameException(String message) {
            super(message);
        }
    }

}
