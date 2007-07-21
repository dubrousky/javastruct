package struct;

import java.io.Serializable;

@StructClass
public class SimpleNested implements Serializable {
	private static final long serialVersionUID = -7431972319124709870L;
	@StructField(order = 0)
	public PublicPrimitives p1;
	@StructField(order = 1)
	public PublicPrimitives p2;
	@StructField(order = 2)
	public PublicPrimitives p3;

	public boolean equals(Object o) {
		SimpleNested other = (SimpleNested) o;
		return (this.p1.equals(other.p1) && this.p2.equals(other.p2) && this.p3
				.equals(other.p3));
	}
}
