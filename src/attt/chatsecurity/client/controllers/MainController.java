package attt.chatsecurity.client.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import attt.chatsecurity.client.models.Security;
import attt.chatsecurity.client.views.MainView;

public class MainController {
	private MainView main;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String name;
	private byte[] ipServer;
	private int port;
	private Security security;
	
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
					input =  new ObjectInputStream(socket.getInputStream());
					output = new ObjectOutputStream(socket.getOutputStream());
					Thread t = new Thread(new Runnable() {

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
					t.start();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
	}
}
