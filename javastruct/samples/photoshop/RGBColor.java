package photoshop;

import struct.StructClass;
import struct.StructField;

@StructClass
public class RGBColor {

	@StructField(order = 0)
	public AString name;

	@StructField(order = 1)
	public byte[] code = new byte[6];

	@StructField(order = 2)
	public byte red;

	@StructField(order = 3)
	public byte green;

	@StructField(order = 4)
	public byte blue;

	public RGBColor(int red, int blue, int green, String name) {
		this.red = (byte) red;
		this.green = (byte) green;
		this.blue = (byte) blue;
		this.name = new AString(name);
	}

	public RGBColor() {
	}

	public String toString() {
		return "Name: " + name + " R:" + ((int) red & 0xFF) + " G:"
				+ ((int) green & 0xFF) + " B:" + ((int) blue & 0xFF);
	}
}
