package struct;

import java.io.Serializable;

@StructClass
public class PrivatePrimitives implements Serializable {
	private static final long serialVersionUID = 8155926269697741970L;

	@StructField(order = 0)
	private byte b;
	@StructField(order = 1)
	private char c;
	@StructField(order = 2)
	private short s;
	@StructField(order = 3)
	private int i;
	@StructField(order = 4)
	private long lo;
	@StructField(order = 5)
	private float f;
	@StructField(order = 6)
	private double d;

	transient int blah;
	transient double foo;

	public void setParams() {
		b = 1;
		c = 'x';
		s = 12;
		i = 123;
		lo = 1000L;
		f = 3.14f;
		d = 3.141d;
	}

	public boolean equals(Object o) {
		PrivatePrimitives other = (PrivatePrimitives) o;
		return (this.b == other.b && this.c == other.c && this.s == other.s
				&& this.i == other.i && this.lo == other.lo
				&& this.f == other.f && this.d == other.d);
	}

	public byte getB() {
		return b;
	}

	public void setB(byte b) {
		this.b = b;
	}

	public char getC() {
		return c;
	}

	public void setC(char c) {
		this.c = c;
	}

	public short getS() {
		return s;
	}

	public void setS(short s) {
		this.s = s;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public long getLo() {
		return lo;
	}

	public void setLo(long lo) {
		this.lo = lo;
	}

	public float getF() {
		return f;
	}

	public void setF(float f) {
		this.f = f;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public int getBlah() {
		return blah;
	}

	public void setBlah(int blah) {
		this.blah = blah;
	}

	public double getFoo() {
		return foo;
	}

	public void setFoo(double foo) {
		this.foo = foo;
	}
}
