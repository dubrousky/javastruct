package mina;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class StructEncoder implements ProtocolEncoder{

	public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) 
	throws Exception {
		byte[] buffer = (byte[])obj;
	    ByteBuffer b = ByteBuffer.allocate(buffer.length + 4, false);
	    b.putInt(buffer.length);
	    b.put(buffer);
	    b.flip();
	    out.write(b);
	}

	public void dispose(IoSession arg0) throws Exception {
	}
}
