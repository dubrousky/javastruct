package mina;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import struct.JavaStruct;

public class StructEncoder implements ProtocolEncoder{

	public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) 
	throws Exception {
		StructMessage m = (StructMessage)obj;
		byte[] buffer = JavaStruct.pack(m);
	    ByteBuffer b = ByteBuffer.allocate(buffer.length + 8, false);
	    b.putInt(buffer.length + 4);
	    b.putInt(m.getID());
	    b.put(buffer);
	    b.flip();
	    out.write(b);
	}

	public void dispose(IoSession arg0) throws Exception {
	}
}
