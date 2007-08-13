package struct;

import junit.framework.TestCase;

public class PerfTests extends TestCase {
	public void testPublicPrimitiveArrraysPerf() {
		PublicPrimitiveArrays o = new PublicPrimitiveArrays();
		o.init(10);
		o.setAsc(10);
		Util.testPerf(o, 100000);
	}

	public void testPublicPrimitivesPerf() {
		Util.testPerf(new PublicPrimitives(), 500000);
	}

	public void testPrivatePrimitivesPerf() {
		Util.testPerf(new PrivatePrimitives(), 500000);
	}
	
	public void testPublicPrimitivesPerf2() {
		Util.testPerf2(new PublicPrimitives(), 500000);
	}

	public void testPrivatePrimitivesPerf2() {
		Util.testPerf2(new PrivatePrimitives(), 500000);
	}

	public void testNestedPublicPrimitivesPerf() {
		PublicPrimitives p1 = new PublicPrimitives();
		PublicPrimitives p2 = new PublicPrimitives();
		PublicPrimitives p3 = new PublicPrimitives();
		p1.setParams();
		p2.setParams();
		p3.setParams();

		SimpleNested n = new SimpleNested();
		n.p1 = p1;
		n.p2 = p2;
		n.p3 = p3;

		Util.testPerf(n, 100000);
	}
}
