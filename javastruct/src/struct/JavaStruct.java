package struct;

public class JavaStruct {
	
	public static final byte[] pack(Object o) throws StructException{
		return pack(o, ByteOrder.BIG_ENDIAN);
	}

	public static final byte[] pack(Object o, ByteOrder order) throws StructException{
		StructPacker packer = new StructPacker(o, order);
		return packer.pack();
	}
	
	public static final void unpack(Object o, byte[] buffer) throws StructException{
		unpack(o, buffer, ByteOrder.BIG_ENDIAN);
	}

	public static final void unpack(Object o, byte[] buffer, ByteOrder order) throws StructException{
		StructUnpacker unpacker = new StructUnpacker(o, buffer, order);
		unpacker.unpack();
	}
	
}
