package struct;

import java.io.Serializable;

@StructClass
public class PublicPrimitiveArrays implements Serializable {
	private static final long serialVersionUID = 8356611007456552686L;
	@StructField(order = 0)
	public byte[] b;
	@StructField(order = 1)
	public char[] c;
	@StructField(order = 2)
	public short[] s;
	@StructField(order = 3)
	public int[] i;
	@StructField(order = 4)
	public long[] lo;
	@StructField(order = 5)
	public float[] f;
	@StructField(order = 6)
	public double[] d;

	public PublicPrimitiveArrays() {
	}

	public void init(int size) {
		b = new byte[size];
		c = new char[size];
		s = new short[size];
		i = new int[size];
		lo = new long[size];
		f = new float[size];
		d = new double[size];
	}

	public void setAsc(int size) {
		for (int j = 0; j < size; j++) {
			b[j] = (byte) j;
			c[j] = (char) j;
			s[j] = (short) j;
			i[j] = j;
			lo[j] = (long) j;
			f[j] = (float) j;
			d[j] = (double) j;
		}
	}

	public void setDesc(int size) {
		for (int j = 0; j < size; j++) {
			b[j] = (byte) (size - j);
			c[j] = (char) (size - j);
			s[j] = (short) (size - j);
			i[j] = (size - j);
			lo[j] = (long) (size - j);
			f[j] = (float) (size - j);
			d[j] = (double) (size - j);
		}
	}

}
