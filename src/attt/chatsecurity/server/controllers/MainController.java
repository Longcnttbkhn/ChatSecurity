package attt.chatsecurity.server.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import attt.chatsecurity.server.models.SecurityServer;
import attt.chatsecurity.server.views.MainView;

public class MainController {
	private MainView main;
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private int port;
	private String name;
	private SecurityServer security;

	public MainController() {
		main = new MainView();
		main.setOnClickNextButton(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				port = main.getPort();
				name = main.getName();
				try {
					main.setIPServer(InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				main.changePanelWait();
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							serverSocket = new ServerSocket(port);
							socket = serverSocket.accept();
							output = new ObjectOutputStream(socket.getOutputStream());
							input = new ObjectInputStream(socket.getInputStream());
							Thread t1 = new Thread(new Runnable() {

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
//										e.printStackTrace();
										JOptionPane.showMessageDialog(null, "Disconnected");
									}
								}
							});
							security = new SecurityServer(input, output);
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
//										e1.printStackTrace();
										JOptionPane.showMessageDialog(null, "Disconnected");
									}
								}
							});
							t1.start();

						} catch (Exception e1) {
							// TODO Auto-generated catch block
//							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "Can't connect to client");
							
						}
					}
				});
				t.start();
			}
		});

	}
}
