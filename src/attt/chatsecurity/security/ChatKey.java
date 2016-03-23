package attt.chatsecurity.security;

import java.math.BigInteger;
import java.security.Key;

public class ChatKey implements Key{

	private byte[] encoded;

	public ChatKey(byte[] encoded) {
		// TODO Auto-generated constructor stub
		this.encoded = encoded;
	}

	public ChatKey(String encoded) {
		// TODO Auto-generated constructor stub
		BigInteger key = new BigInteger(encoded);
		this.encoded = key.toByteArray();
	}

	@Override
	public String getAlgorithm() {
		// TODO Auto-generated method stub
		return "DES";
	}

	@Override
	public String getFormat() {
		// TODO Auto-generated method stub
		return "RAW";
	}

	@Override
	public byte[] getEncoded() {
		// TODO Auto-generated method stub
		return encoded;
	}

}
