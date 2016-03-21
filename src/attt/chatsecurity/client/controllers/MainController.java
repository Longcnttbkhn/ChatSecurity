package attt.chatsecurity.client.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import attt.chatsecurity.client.views.MainView;

public class MainController {
	private MainView main;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private String name;
	private byte[] ipServer;
	private int port;
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
					input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					output = new PrintWriter(socket.getOutputStream());
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String message;
							while (true) {
								try {
									message = input.readLine();
									if (message == null)
										break;
									main.showText(message + "\n");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}
					});
					main.changePanelChat();
					main.setOnClickSendButton(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							String message;
							message = main.getMessage();
							output.println(name + ": " + message); 
							main.showText("bạn: " + message + "\n");
							output.flush();
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
