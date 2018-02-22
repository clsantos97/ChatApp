package ui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;
import logic.server.Manager;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ServerView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private JButton btnExit;
	private Manager manager = new Manager();
	private JList<String> userList;
	private JButton btnKick;
	private DefaultListModel<String> model;
	private final ServerView SERVER_VIEW = this;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerView frame = new ServerView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerView() {
		setTitle("Server window");
		//setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		setBounds(10, 10, 650, 500);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
                contentPane.setBackground(Color.gray);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setAutoscrolls(true);

		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(10, 20, 422, 400);
		contentPane.add(scroll);

		model = new DefaultListModel<>();
		userList = new JList<>(model);

		JScrollPane listScrool = new JScrollPane(userList);
		listScrool.setBounds(440, 20, 195, 400);
		contentPane.add(listScrool);

		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		btnExit = new JButton("Salir");
		btnExit.setBounds(332, 430, 100, 24);
                btnExit.setBackground(Color.white);
		contentPane.add(btnExit);

		btnKick = new JButton("Expulsar");
		btnKick.setBounds(440, 430, 195, 24);
		btnKick.setEnabled(false);
                btnKick.setBackground(Color.white);
                //btnKick.setForeground(Color.WHITE);
		contentPane.add(btnKick);
                
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                userList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (userList.getSelectedIndex() == -1) {
					btnKick.setEnabled(false);
				} else {
					btnKick.setEnabled(true);
				}
			}
		});
                
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					manager.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				SERVER_VIEW.dispose();
			}
		});

		btnKick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manager.kick(userList.getSelectedValue());
			}
		});

		

		this.manager.start(textArea, model);
	}
}
