package attt.chatsecurity.client.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.StringTokenizer;

import javax.crypto.SealedObject;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import attt.chatsecurity.security.ChatKey;
import attt.chatsecurity.security.Security;

public class SecurityClient extends Security {

	private String fileSecurityName = "Client_security.txt";

	public SecurityClient(ObjectInputStream input, ObjectOutputStream output) throws Exception {
		File file = new File(fileSecurityName);
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				ChatKey security_key = new ChatKey(reader.readLine());
				SecureRandom secureRandom = new SecureRandom();
				Long random1 = (Long) decryption((SealedObject) input.readObject(), security_key);
				random1 -= 1;
				Long random2 = secureRandom.nextLong();
				String send = random1 + "|" + random2;
				output.writeObject(encryption(send, security_key));
				output.flush();

				String receive = (String) decryption((SealedObject) input.readObject(), security_key);
				StringTokenizer strToken = new StringTokenizer(receive, "|");
				random2 -= 1;
				Long random2_ = new Long(strToken.nextToken());
				if (random2.equals(random2_)) {
					BigInteger key = new BigInteger(strToken.nextToken());
					section_key = new ChatKey(key.toByteArray());
				} else {
					throw new Exception("Kết nối không an toàn");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (file.createNewFile()) {
				try (PrintWriter writer = new PrintWriter(file)) {
					Object inputObject = input.readObject();
					
					JOptionPane.showMessageDialog(null,
							"Server yêu cầu nhập mật khẩu \n" + "nhập mật khẩu để kết nối", "Client-Message",
							JOptionPane.WARNING_MESSAGE);
					JPasswordField password = new JPasswordField(10);
					int action = JOptionPane.showConfirmDialog(null, password, "Enter password",
							JOptionPane.OK_CANCEL_OPTION);
					if (action == 0) {
						String pass = new String(password.getPassword());
						ChatKey unsafe_key = new ChatKey(hash(pass));
						SecureRandom secureRandom = new SecureRandom();
						
						Long random1 = (Long) decryption((SealedObject) inputObject, unsafe_key);
						random1 -= 1;
						Long random2 = secureRandom.nextLong();
						String send = random1 + "|" + random2;
						output.writeObject(encryption(send, unsafe_key));
						output.flush();

						String receive = (String) decryption((SealedObject) input.readObject(), unsafe_key);
						StringTokenizer strToken = new StringTokenizer(receive, "|");
						random2 -= 1;
						Long random2_ = new Long(strToken.nextToken());
						if (random2.equals(random2_)) {
							BigInteger p = new BigInteger(strToken.nextToken());
							BigInteger a = new BigInteger(strToken.nextToken());
							BigInteger Ya = new BigInteger(strToken.nextToken());
							BigInteger Xb = new BigInteger(32, secureRandom);
							BigInteger Yb = a.modPow(Xb, p);
							output.writeObject(encryption(Yb, unsafe_key));
							output.flush();
							
							BigInteger secu_key = Ya.modPow(Xb, p);
							ChatKey security_key = new ChatKey(secu_key.toByteArray());
							
							BigInteger sect_key = (BigInteger) decryption((SealedObject)input.readObject(), security_key);
							section_key = new ChatKey(sect_key.toByteArray());
							writer.print(secu_key.toString());
						} else 
							throw new Exception("Ket noi khong an toan");
					} else
						throw new Exception();	
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
