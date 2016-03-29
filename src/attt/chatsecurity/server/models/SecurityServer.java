package attt.chatsecurity.server.models;

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

public class SecurityServer extends Security {

	private String fileSecurityName = "Server_security.txt";
	

	public SecurityServer(ObjectInputStream input, ObjectOutputStream output) throws Exception {
		File file = new File(fileSecurityName);
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				ChatKey security_key = new ChatKey(reader.readLine());
				SecureRandom secureRandom = new SecureRandom();
				Long random1 = secureRandom.nextLong();
				output.writeObject(encryption(random1, security_key));
				output.flush();

				String receive = (String) decryption((SealedObject) input.readObject(), security_key);
				StringTokenizer strToken = new StringTokenizer(receive, "|");
				random1 -= 1;
				Long random1_ = new Long(strToken.nextToken());
				if (random1.equals(random1_)) {
					Long random2 = new Long(strToken.nextToken());
					random2 -= 1;
					BigInteger key = new BigInteger(63, secureRandom);
					section_key = new ChatKey(key.toByteArray());
					String send = random2 + "|" + key;
					output.writeObject(encryption(send, security_key));
					output.flush();
				} else {
					throw new Exception("Kết nối không an toàn");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (file.createNewFile()) {
				try (PrintWriter writer = new PrintWriter(file)) {
					JOptionPane.showMessageDialog(null, "Bạn chưa thiết lập kết nối an toàn \n"
							+ "Nhập mật khẩu để thiết lập kết nối an toàn", "Server-Message", JOptionPane.WARNING_MESSAGE);
					JPasswordField password = new JPasswordField(10);
					int action = JOptionPane.showConfirmDialog(null, password, "Enter password", JOptionPane.OK_CANCEL_OPTION);
					if (action == 0) {
						String pass = new String(password.getPassword());
						ChatKey unsafe_key = new ChatKey(hash(pass));
						SecureRandom secureRandom = new SecureRandom();
						Long random1 = secureRandom.nextLong();
						output.writeObject(encryption(random1, unsafe_key));
						output.flush();

						String receive = (String) decryption((SealedObject) input.readObject(), unsafe_key);
						StringTokenizer strToken = new StringTokenizer(receive, "|");
						random1 -= 1;
						Long random1_ = new Long(strToken.nextToken());
						if (random1.equals(random1_)) {
							Long random2 = new Long(strToken.nextToken());
							random2 -= 1;
							BigInteger p = BigInteger.probablePrime(64, secureRandom);
							BigInteger a = new BigInteger(32, secureRandom);
							BigInteger Xa = new BigInteger(32, secureRandom);
							BigInteger Ya = a.modPow(Xa, p);
							
							String send = random2 + "|" + p + "|" + a + "|" + Ya;
							output.writeObject(encryption(send, unsafe_key));
							output.flush();
							
							BigInteger Yb = (BigInteger) decryption((SealedObject)input.readObject(), unsafe_key);
							BigInteger secu_key = Yb.modPow(Xa, p);
							ChatKey security_key = new ChatKey(secu_key.toByteArray());
							BigInteger sect_key = new BigInteger(63, secureRandom);
							section_key = new ChatKey(sect_key.toByteArray());
							output.writeObject(encryption(sect_key, security_key));
							output.flush();
							writer.print(secu_key.toString());
						} else
							throw new Exception("Ket noi khong an toan");
					} else
						throw new Exception();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				throw new Exception();
		}
	}
}