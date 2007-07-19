package mina;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class StructDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, ByteBuffer buffer,
			ProtocolDecoderOutput out) throws Exception {
		
		
		return false;
	}

}
