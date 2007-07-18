package photoshop;

import struct.StructClass;
import struct.StructField;

@StructClass
public class RGBColor{
	
	@StructField (order = 0)
	public AString name;
	
	@StructField (order = 1)
	public byte[] code = new byte[6];	
	
	@StructField (order = 2)
	public byte red;

	@StructField (order = 3)
	public byte green;

	@StructField (order = 4)
	public byte blue;
	
	public RGBColor(int red, int blue, int green){
		this.red = (byte)red;
		this.green = (byte)green;
		this.blue = (byte)blue;
		this.name = new AString("R:" + red + " G:" + green + " B:" + blue);
	}
	
	public String toString(){
		return "R:" + red + " G:" + green + " B:" + blue;
	}
}
