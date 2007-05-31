package struct;

import java.io.Serializable;

import struct.StructClass;
import struct.StructField;

@SuppressWarnings("serial")
@StructClass
public class PublicPrimitives implements Serializable {
	@StructField(order = 0)
	public byte b;
	@StructField(order = 1)
	public char c;
	@StructField(order = 2)
	public short s;
	@StructField(order = 3)
	public int i;
	@StructField(order = 4)
	public long lo;
	@StructField(order = 5)
	public float f;
	@StructField(order = 6)
	public double d;
	
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
	
	public boolean equals(Object o){
		PublicPrimitives other = (PublicPrimitives)o;
		return (this.b == other.b 
				&& this.c == other.c
				&& this.s == other.s
				&& this.i == other.i
				&& this.lo == other.lo
				&& this.f == other.f
				&& this.d == other.d);
	}
}