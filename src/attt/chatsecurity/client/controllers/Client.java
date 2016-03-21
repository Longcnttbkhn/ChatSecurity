package attt.chatsecurity.client.controllers;

import javax.swing.SwingUtilities;

import attt.chatsecurity.client.controllers.MainController;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new MainController();
			}
		});
	}

}
