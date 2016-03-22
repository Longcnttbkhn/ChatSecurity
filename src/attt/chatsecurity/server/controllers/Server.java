package attt.chatsecurity.server.controllers;

import javax.swing.SwingUtilities;

public class Server {
	
	public static void main(String arg[]) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new MainController();
			}
		});
	}
}
