package attt.chatsecurity.server.views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class MainView extends JFrame {
	private JTextField tfPort;
	private JTextField tfName;
	private JTextField tfMessage;

	private JTextArea taShowChat;

	private JButton btnNext;
	private JButton btnSend;

	private JLabel lbIPServer;
	private JLabel lbPort;

	private JFrame main;
	private Container cp;
	private JPanel panelNhapThongTin;
	private JPanel panelWait;
	private JPanel panelChat;

	public MainView() {
		main = this;

		panelNhapThongTin = new JPanel();

		panelNhapThongTin.setBorder(new TitledBorder("Nhập thông tin server"));
		panelNhapThongTin.setLayout(new GridLayout(3, 2));
		panelNhapThongTin.add(new JLabel("Port"));
		tfPort = new JTextField(20);
		panelNhapThongTin.add(tfPort);
		panelNhapThongTin.add(new JLabel("Your name"));
		tfName = new JTextField(20);
		panelNhapThongTin.add(tfName);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				main.dispose();
			}
		});
		panelNhapThongTin.add(btnExit);

		btnNext = new JButton("Next");
		panelNhapThongTin.add(btnNext);

		panelWait = new JPanel();
		panelWait.setBorder(new TitledBorder("Waiting for client"));
		panelWait.setLayout(new GridLayout(3, 1));
		lbIPServer = new JLabel("");
		panelWait.add(lbIPServer);
		lbPort = new JLabel();
		panelWait.add(lbPort);
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		panelWait.add(btnCancel);

		panelChat = new JPanel();
		panelChat.setBorder(new TitledBorder("Chatting"));
		panelChat.setLayout(new BorderLayout());
		taShowChat = new JTextArea(10, 25);
		taShowChat.setLineWrap(true);
		taShowChat.setWrapStyleWord(true);
		panelChat.add(new JScrollPane(taShowChat), BorderLayout.NORTH);
		tfMessage = new JTextField(21);
		tfMessage.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					btnSend.doClick();
			}
		});
		panelChat.add(tfMessage, BorderLayout.WEST);
		btnSend = new JButton("Send");
		panelChat.add(btnSend, BorderLayout.EAST);

		setTitle("Server");
		cp = getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(panelNhapThongTin);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public int getPort() {
		if (tfPort.getText() != null) {
			lbPort.setText("Port: " + tfPort.getText());
			return Integer.parseInt(tfPort.getText());
		} else
			return -1;
	}

	public String getName() {
		return tfName.getText();
	}

	public void setOnClickNextButton(ActionListener action) {
		btnNext.addActionListener(action);
	}

	public void setIPServer(String ip) {
		lbIPServer.setText("IP Server: " + ip);
	}

	public String getMessage() {
		String message = tfMessage.getText();
		tfMessage.setText(null);
		return message;
	}

	public void showText(String message) {
		taShowChat.append(message);
	}

	public void setOnClickSendButton(ActionListener action) {
		btnSend.addActionListener(action);
	}

	public void changePanelWait() {
		panelNhapThongTin.setVisible(false);
		cp.add(panelWait);
		pack();
	}

	public void changePanelChat() {
		panelWait.setVisible(false);
		cp.add(panelChat);
		pack();
	}
}
