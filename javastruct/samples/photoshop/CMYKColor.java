package photoshop;

import struct.StructClass;
import struct.StructField;

@StructClass
public class CMYKColor {

	@StructField(order = 0)
	public AString name;

	@StructField(order = 1)
	public byte[] code = new byte[6];

	@StructField(order = 2)
	public byte cyan;

	@StructField(order = 3)
	public byte magenta;

	@StructField(order = 4)
	public byte yellow;

	@StructField(order = 5)
	public byte key;

	public CMYKColor(int cyan, int magenta, int yellow, int key, String name) {
		this.cyan = (byte) cyan;
		this.magenta = (byte) magenta;
		this.yellow = (byte) yellow;
		this.key = (byte) key;
		this.name = new AString(name);
	}

	public CMYKColor() {
	}

	public String toString() {
		return "Name : " + name + " C:" + ((int) cyan & 0xFF) + " M:"
				+ ((int) magenta & 0xFF) + " Y:" + ((int) yellow & 0xFF)
				+ " K:" + ((int) key & 0xFF);
	}
}
