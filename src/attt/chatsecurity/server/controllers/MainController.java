package attt.chatsecurity.server.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import attt.chatsecurity.server.views.MainView;

public class MainController {
	private MainView main;
	private ServerSocket serverSocket;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private int port;
	private String name;

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
							input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							output = new PrintWriter(socket.getOutputStream());
							Thread t1 = new Thread(new Runnable() {

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
