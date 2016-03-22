package attt.chatsecurity.server.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import attt.chatsecurity.server.models.Security;
import attt.chatsecurity.server.views.MainView;

public class MainController {
	private MainView main;
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private int port;
	private String name;
	private Security security;
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
							input = new ObjectInputStream(socket.getInputStream());
							output = new ObjectOutputStream(socket.getOutputStream());
							Thread t1 = new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									String message;
									while (true) {
										try {
											message = (String) input.readObject();
											if (message == null)
												break;
											main.showText(message + "\n");
										} catch (IOException | ClassNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									}
								}
							});
							security = new Security(input, output);
							main.changePanelChat();
							main.setOnClickSendButton(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub
									String message;
									message = main.getMessage();
									try {
										output.writeObject(message);
										main.showText("bạn: " + message + "\n");
										output.flush();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}	
								}
							});
							t1.start();
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				t.start();
			}
		});

}}
