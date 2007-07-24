package mina;

import java.util.HashMap;

import struct.StructException;

public class Messages {
	public static final int IMAGE_REQUEST = 1;
	public static final int IMAGE_RESPONSE = 2;
	
	public static HashMap<Integer, Class>types = new HashMap<Integer, Class>();
	static{
		types.put(IMAGE_REQUEST, ImageRequest.class);
		types.put(IMAGE_RESPONSE, ImageResponse.class);
	}
	
	public static StructMessage getMessage(int type) throws StructException{
		try {
			Class c = types.get(type);
			if(c != null){
				return  (StructMessage)c.newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new StructException("Failed");
	}
	
	
}
