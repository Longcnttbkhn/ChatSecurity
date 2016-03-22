package attt.chatsecurity.server.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

public class Security {
	
	private String fileSecurityName = "Server_security.txt";
	private byte[] security_key;
//	private byte[] section_key;
	
	public Security(ObjectInputStream input, ObjectOutputStream output) {
		File file = new File(fileSecurityName);
		if (file.exists()){
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				security_key = new BigInteger(reader.readLine()).toByteArray();
				BigInteger random1 = new BigInteger(32, new SecureRandom());
				System.out.println(random1);
				output.writeObject(encryption(random1.toString(), security_key));
				output.flush();
				
				SealedObject r1_r2 = (SealedObject) input.readObject();
				
//				section_key = new BigInteger(64, new SecureRandom()).toByteArray();
				
				
			} catch (IOException | ClassNotFoundException e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	/*public String decryption(String ciphertext){
		String plaintext = null;
		return plaintext;
	}
	
	public String encryption(String plaintext) {
		String ciphertext = null;
		return ciphertext;
	}*/
	
	public SealedObject encryption(Serializable ciphertext, byte[] encoded) {
		Cipher cipher;
		SealedObject sealedObject = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, new ChatKey(encoded));
			sealedObject = new SealedObject(ciphertext, cipher);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | IOException
				| InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sealedObject;
	}
}

class ChatKey implements Key{
	private byte[] encoded;
	public ChatKey(byte[] encoded) {
		// TODO Auto-generated constructor stub
		this.encoded = encoded;
	}

	@Override
	public String getAlgorithm() {
		// TODO Auto-generated method stub
		return "DES";
	}

	@Override
	public String getFormat() {
		// TODO Auto-generated method stub
		return "PKCS#8";
	}

	@Override
	public byte[] getEncoded() {
		// TODO Auto-generated method stub
		return encoded;
	}
	
}







