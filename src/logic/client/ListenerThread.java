package logic.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JButton;
import javax.swing.JTextArea;
import logic.cfg.ChatAppConstants;
import model.Message;

public class ListenerThread extends Thread {

    ObjectInputStream input = null;
    JTextArea textArea;
    JButton btnSend;
    Socket socket = null;

    ListenerThread(Socket socket, String nickName, JTextArea textArea, JButton btnSend, ObjectInputStream input)
            throws IOException {
        this.setName(nickName);
        this.socket = socket;
        this.textArea = textArea;
        this.btnSend = btnSend;
        this.input = input;

        start();
    }

    @Override
    public void run() {
        boolean kicked = false;
        try {
            while (true) {
                Message message = (Message) input.readObject();
                textArea.append(message.toString());

                if (message.getMsg().equals(ChatAppConstants.DISCONNECTED) || message.getMsg().equals(ChatAppConstants.MSG_USER_KICKED)) {
                    kicked = true;
                    break;
                }
            }
        } catch (SocketException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            btnSend.setEnabled(false);

            if (!kicked) {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
