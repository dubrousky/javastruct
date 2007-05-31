package struct;

import struct.ArrayLengthMarker;
import struct.StructClass;
import struct.StructField;

@StructClass
public class LengthMarkers {
	@ArrayLengthMarker
	@StructField(order = 0)
	public int bufferLength;
	@StructField(order = 1)
	public byte[] buffer;
	
	public LengthMarkers(){
		
	}
}
