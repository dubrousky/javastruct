package photoshop;

import struct.StructClass;
import struct.StructField;

@StructClass
public class ColorRecords {
	@StructField (order = 0)
	public Color[] colors;
}
