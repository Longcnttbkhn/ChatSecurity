package attt.chatsecurity.client.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import attt.chatsecurity.client.models.SecurityClient;
import attt.chatsecurity.client.views.MainView;

public class MainController {
	private MainView main;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String name;
	private byte[] ipServer;
	private int port;
	private SecurityClient security;

	public MainController() {
		main = new MainView();
		main.setOnClickNextButton(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				name = main.getName();
				port = main.getPort();
				ipServer = main.getIpServer();
				try {
					socket = new Socket(InetAddress.getByAddress(ipServer), port);
					output = new ObjectOutputStream(socket.getOutputStream());
					input = new ObjectInputStream(socket.getInputStream());
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String message;
							try {
								while (true) {
									message = security.decryption(input.readObject());
									main.showText(message + "\n");
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
//								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "Disconnected");
							}
						}
					});
					security = new SecurityClient(input, output);
					main.changePanelChat();
					main.setOnClickSendButton(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							String message;
							message = main.getMessage();
							main.showText("bạn: " + message + "\n");
							try {
								output.writeObject(security.encryption(name + ": " + message));
								output.flush();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
//								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, "Disconnected");
							}
						}
					});
					t.start();
				} catch (Exception e1) {
//					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Can't connect to server");
				}
					
			}
		});

	}
}
