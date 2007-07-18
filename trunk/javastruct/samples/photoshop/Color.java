package photoshop;

import struct.StructClass;
import struct.StructField;

@StructClass
public class Color {
	@StructField (order = 0)
	public AString name;
	
	@StructField (order = 1)
	public byte[] code = new byte[6];
}
