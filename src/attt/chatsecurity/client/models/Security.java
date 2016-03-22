package attt.chatsecurity.client.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

public class Security {

	private String fileSecurityName = "Client_security.txt";
	private byte[] security_key;

	public Security(ObjectInputStream input, ObjectOutputStream output) {
		File file = new File(fileSecurityName);
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				security_key = new BigInteger(reader.readLine()).toByteArray();
				SealedObject sealedObject = (SealedObject) input.readObject();
				BigInteger random1 = new BigInteger(decryption(sealedObject, security_key));
				System.out.println(random1);
				
				sealedObject = (SealedObject)input.readObject();
				
			} catch (IOException | ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/*
	 * public String decryption(String ciphertext){ String plaintext = null;
	 * return plaintext; }
	 * 
	 * public String encryption(String plaintext) { String ciphertext = null;
	 * return ciphertext; }
	 */
	public SealedObject encryption(Serializable plaintext, byte[] encoded) {
		Cipher cipher;
		SealedObject ciphertext = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, new ChatKey(encoded));
			ciphertext = new SealedObject(plaintext, cipher);
		} catch (NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ciphertext;
	}

	public String decryption(SealedObject ciphertext, byte[] encoded) {
		Cipher cipher;
		String plaintext = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, new ChatKey(encoded));
			plaintext = (String) ciphertext.getObject(cipher);
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return plaintext;
	}
}

class ChatKey implements Key {
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
