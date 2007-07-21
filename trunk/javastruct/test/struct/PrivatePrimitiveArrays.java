package struct;

import java.io.Serializable;

@StructClass
public class PrivatePrimitiveArrays implements Serializable {
	private static final long serialVersionUID = 5211328614125606654L;

	@StructField(order = 0)
	private byte[] b;
	@StructField(order = 1)
	private char[] c;
	@StructField(order = 2)
	private short[] s;
	@StructField(order = 3)
	private int[] i;
	@StructField(order = 4)
	private long[] lo;
	@StructField(order = 5)
	private float[] f;
	@StructField(order = 6)
	private double[] d;

	public PrivatePrimitiveArrays() {
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

	public byte[] getB() {
		return b;
	}

	public void setB(byte[] b) {
		this.b = b;
	}

	public char[] getC() {
		return c;
	}

	public void setC(char[] c) {
		this.c = c;
	}

	public short[] getS() {
		return s;
	}

	public void setS(short[] s) {
		this.s = s;
	}

	public int[] getI() {
		return i;
	}

	public void setI(int[] i) {
		this.i = i;
	}

	public long[] getLo() {
		return lo;
	}

	public void setLo(long[] lo) {
		this.lo = lo;
	}

	public float[] getF() {
		return f;
	}

	public void setF(float[] f) {
		this.f = f;
	}

	public double[] getD() {
		return d;
	}

	public void setD(double[] d) {
		this.d = d;
	}

}
