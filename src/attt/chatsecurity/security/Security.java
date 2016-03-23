package attt.chatsecurity.security;

import java.io.Serializable;
import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;

public class Security {
	protected ChatKey section_key;

	public SealedObject encryption(String plaintext) throws Exception{
		return encryption(plaintext, section_key);
	}

	public String decryption(Object ciphertext) throws Exception{
		return (String) decryption((SealedObject) ciphertext, section_key);
	}

	public SealedObject encryption(Serializable plaintext, ChatKey key) throws Exception {
		SealedObject ciphertext = null;
		Cipher cipher;
		cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		ciphertext = new SealedObject(plaintext, cipher);
		return ciphertext;
	}

	public Object decryption(SealedObject ciphertext, ChatKey key) throws Exception{
		Object plaintext = null;
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		plaintext = ciphertext.getObject(cipher);
		return plaintext;

	}
	public byte[] hash(String string) {
		  Long h = 4371791430146274081L; // prime
		  int len = string.length();

		  for (int i = 0; i < len; i++) {
		    h = 31*h + string.charAt(i);
		  }
		  BigInteger big = new BigInteger(h.toString());
		  return big.toByteArray();
	}
}
