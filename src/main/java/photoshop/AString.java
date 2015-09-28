package photoshop;

import struct.ArrayLengthMarker;
import struct.StructClass;
import struct.StructField;

@StructClass
public class AString {

	@StructField(order = 0)
	@ArrayLengthMarker(fieldName = "chars")
	public int length;

	@StructField(order = 1)
	public char[] chars;

	public AString(String content) {
		this.length = content.length();
		this.chars = content.toCharArray();
	}

	public AString() {
	}

	public String toString() {
		return new String(chars);
	}
}
