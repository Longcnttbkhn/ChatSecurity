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

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import attt.chatsecurity.server.views.MainView;

public class MainController {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainView main = new MainView();
				main.setOnClickNextButton(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						try {
							InetAddress inet = InetAddress.getLocalHost();
							main.setIPServer(inet.getAddress());
							int port = main.getPort();
							String name = main.getName();
							main.changePanelWait();
							Thread t = new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try (ServerSocket serverSocket = new ServerSocket(port);
											Socket socket = serverSocket.accept();
											BufferedReader input = new BufferedReader(
													new InputStreamReader(socket.getInputStream()));
											PrintWriter output = new PrintWriter(socket.getOutputStream());) {
										Thread t1 = new Thread(new Runnable() {
											public void run() {
												String message;
												while (true) {
													try {
														message = input.readLine();
														if (message == null)
															break;
														main.showText(message);
													} catch (IOException e) {
														// TODO Auto-generated catch
														// block
														e.printStackTrace();
													}
												}
											}
										});
										t1.start();
										main.changePanelChat();
										main.setOnClickSendButton(new ActionListener() {
											
											@Override
											public void actionPerformed(ActionEvent e) {
												// TODO Auto-generated method stub
												String message;
												message = main.getMessage();
												output.println(name + ": " + message); 			// sử dụng ouput để gửi
												main.showText("bạn: " + message + "\n");
												output.flush();
											}
										});
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							});
							t.start();
						} catch (UnknownHostException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
				});

			}
		});
	}

}
