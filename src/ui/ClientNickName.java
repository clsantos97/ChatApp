package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Component;
import logic.client.Manager;
import logic.client.Manager.NicknameException;

public class ClientNickName extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textNickName;
	private JLabel lblError;
	private JButton btnAccept;
	private Manager manager = new Manager();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientNickName frame = new ClientNickName();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientNickName() {
		setTitle("Client nickname");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		setBounds(100, 400, 268, 166);
		JPanel contentPane = new JPanel();
                contentPane.setBackground(Color.gray);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNickText = new JLabel("nickname:");
		lblNickText.setBounds(44, 11, 166, 14);
		contentPane.add(lblNickText);

		textNickName = new JTextField();
		textNickName.setBounds(44, 36, 166, 20);
		contentPane.add(textNickName);
		textNickName.setColumns(10);

		btnAccept = new JButton("Ok");
		btnAccept.setBounds(87, 85, 89, 23);
		contentPane.add(btnAccept);

		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setBounds(43, 60, 190, 14);
		contentPane.add(lblError);

                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
		btnAccept.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (!textNickName.getText().trim().equals("")) {
					try {
						manager.connect(textNickName.getText());
						ClientView clientGui = new ClientView(textNickName.getText(), manager);
						clientGui.setTitle(textNickName.getText());
						clientGui.setVisible(true);

						Component c = (Component) event.getSource();
						JOptionPane.getFrameForComponent(c).dispose();
					} catch (ConnectException e) {
						lblError.setText("Ha ocurrido un error en el servidor.");
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (NicknameException e) {
						// Display nickname error message.
						lblError.setText(e.getMessage());
					}
				} else {
					lblError.setText("Debes tener un nickname.");
				}
			}
		});
	}
}
