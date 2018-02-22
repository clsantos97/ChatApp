package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import model.Message;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import logic.cfg.ChatAppConstants;
import logic.client.Manager;

public class ClientView extends JFrame {

    private static final long serialVersionUID = 1L;

    private String nickname;
    private Manager manager;
    private JTextField textField;
    private JTextArea textArea;
    private JButton btnSend;
    private JButton btnExit;

    private final ClientView CLIENT_VIEW = this;

    public ClientView(String nickname, Manager manager) {

        this.nickname = nickname;
        this.manager = manager;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        Random rand = new Random();
        int x = 500 + rand.nextInt(700 + 1 - 500);
        int y = rand.nextInt(500);
        setBounds(x, y, 450, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setBackground(Color.gray);
        contentPane.setLayout(null);

        textField = new JTextField();
        textField.setBounds(10, 11, 321, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        btnSend = new JButton("Enviar");
        btnSend.setBounds(341, 10, 89, 23);
        contentPane.add(btnSend);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setAutoscrolls(true);

        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBounds(10, 39, 420, 179);
        contentPane.add(scroll);

        btnExit = new JButton("Salir");
        btnExit.setBounds(179, 232, 89, 23);
        contentPane.add(btnExit);
        
        // Add window listener by implementing WindowAdapter class to
        // the frame instance. To handle the close event we just need
        // to implement the windowClosing() method.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    manager.sendMessage(new Message(CLIENT_VIEW.nickname, ChatAppConstants.DISCONNECTED));
                } catch (IOException ex) {
                    Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            }
        });


        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!textField.getText().trim().isEmpty()) {
                    try {
                        manager.sendMessage(new Message(CLIENT_VIEW.nickname, textField.getText()));
                    } catch (SocketException e) {

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    textField.setText(null);
                }
            }
        });

        btnExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    manager.disconnect(nickname);
                } catch (SocketException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CLIENT_VIEW.dispose();
                }
            }
        });

        textField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent event) {
                // Send msg on enter
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!textField.getText().trim().equals("")) {
                        try {
                            manager.sendMessage(new Message(CLIENT_VIEW.nickname, textField.getText()));
                        } catch (SocketException e) {

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        textField.setText(null);
                    }
                }
            }
        });

        try {
            this.manager.start(this.nickname, this.textArea, this.btnSend);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
