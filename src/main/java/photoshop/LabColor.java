package photoshop;

import struct.StructClass;
import struct.StructField;

@StructClass
public class LabColor {

	@StructField(order = 0)
	public AString name;

	@StructField(order = 1)
	public byte[] code = new byte[6];

	@StructField(order = 2)
	public byte lightness;

	@StructField(order = 3)
	public byte aChrom;

	@StructField(order = 4)
	public byte bChrom;

	public LabColor(int lightness, int aChrom, int bChrom, String name) {
		this.lightness = (byte) lightness;
		this.aChrom = (byte) aChrom;
		this.bChrom = (byte) bChrom;
		this.name = new AString(name);
	}

	public LabColor() {
	}

	public String toString() {
		return "Name:" + name + " L:" + ((int) lightness & 0xFF) + " A:"
				+ ((int) aChrom & 0xFF) + " B:" + ((int) bChrom & 0xFF);
	}
}
