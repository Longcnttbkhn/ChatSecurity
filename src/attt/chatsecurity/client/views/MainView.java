package attt.chatsecurity.client.views;

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

	private JTextField[] tfIp;
	private JFrame main;
	private Container cp;
	private JPanel panelNhapThongTin;
	private JPanel panelChat;

	public MainView() {
		main = this;
		panelNhapThongTin = new JPanel();

		panelNhapThongTin.setBorder(new TitledBorder("Nhap thong tin client"));
		panelNhapThongTin.setLayout(new GridLayout(4, 2, 0, 3));
		panelNhapThongTin.add(new JLabel("IP server"));
		JPanel jpIp = new JPanel(new GridLayout(1, 4, 1, 0));
		tfIp = new JTextField[4];
		tfIp[0] = new JTextField(4);
		tfIp[1] = new JTextField(4);
		tfIp[2] = new JTextField(4);
		tfIp[3] = new JTextField(4);
		jpIp.add(tfIp[0]);
		jpIp.add(tfIp[1]);
		jpIp.add(tfIp[2]);
		jpIp.add(tfIp[3]);
		panelNhapThongTin.add(jpIp);
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

		setTitle("Client");
		cp = getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(panelNhapThongTin);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public byte[] getIpServer() {
		byte[] ip = new byte[4];
		ip[0] = Byte.parseByte(tfIp[0].getText());
		ip[1] = Byte.parseByte(tfIp[1].getText());
		ip[2] = Byte.parseByte(tfIp[2].getText());
		ip[3] = Byte.parseByte(tfIp[3].getText());
		return ip;
	}

	public String getName() {
		return tfName.getText();
	}

	public int getPort() {
		if (tfPort.getText() != null) {
			return Integer.parseInt(tfPort.getText());
		} else
			return -1;
	}

	public String getMessage() {
		String message = tfMessage.getText();
		tfMessage.setText(null);
		return message;
	}

	public void setOnClickNextButton(ActionListener action) {
		btnNext.addActionListener(action);
	}

	public void showText(String message) {
		taShowChat.append(message);
	}

	public void setOnClickSendButton(ActionListener action) {
		btnSend.addActionListener(action);
	}

	public void changePanelChat() {
		panelNhapThongTin.setVisible(false);
		cp.add(panelChat);
		pack();
	}
}
