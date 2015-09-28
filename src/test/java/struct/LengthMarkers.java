package struct;

@StructClass
public class LengthMarkers {
	@ArrayLengthMarker(fieldName = "buffer")
	@StructField(order = 0)
	public int bufferLength;
	@StructField(order = 1)
	public byte[] buffer;

	public LengthMarkers() {
	}

	public void init(int len) {
		bufferLength = len;
		buffer = new byte[len];
	}

	public void fill() {
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = (byte) i;
		}
	}

	public boolean equals(Object o) {
		LengthMarkers lm = (LengthMarkers) o;
		if (o == null)
			return false;
		return Util.arraysEqual(this.buffer, lm.buffer);
	}
}
