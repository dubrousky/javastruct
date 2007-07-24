package mina;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import struct.JavaStruct;

public class StructDecoder extends CumulativeProtocolDecoder {

	public static final int MAX_MESSAGE_SIZE = 5 * 1024 * 1024;
	boolean prefixRead = false;
	
	@Override
	protected boolean doDecode(IoSession session, ByteBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		
		if(in.prefixedDataAvailable(4, MAX_MESSAGE_SIZE)){
			int len = in.getInt();
			int type = in.getInt();
			byte[] b = new byte[len];
			in.get(b);
			StructMessage m = Messages.getMessage(type);
			JavaStruct.unpack(m, b);
			out.write(m);
			return true;
		}
		
		return false;
	}

}
