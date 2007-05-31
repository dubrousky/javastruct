package struct;

import java.io.Serializable;

import struct.StructClass;
import struct.StructField;

@SuppressWarnings("serial")
@StructClass
public class SimpleNested implements Serializable {
	@StructField(order = 0)
	public PublicPrimitives p1;
	@StructField(order = 1)
	public PublicPrimitives p2;
	@StructField(order = 2)
	public PublicPrimitives p3;
	
	public boolean equals(Object o){
		SimpleNested other = (SimpleNested)o;
		return (this.p1.equals(other.p1) 
				&& this.p2.equals(other.p2)
				&& this.p3.equals(other.p3));
	}
}
