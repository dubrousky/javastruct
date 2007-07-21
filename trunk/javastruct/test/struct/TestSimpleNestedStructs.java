package struct;

import junit.framework.TestCase;

public class TestSimpleNestedStructs extends TestCase {

	public void testPublicSimpleNestedStruct() {

		PublicPrimitives p1 = new PublicPrimitives();
		PublicPrimitives p2 = new PublicPrimitives();
		PublicPrimitives p3 = new PublicPrimitives();

		p1.setParams();
		p2.setParams();
		p3.setParams();

		PublicPrimitives p4 = new PublicPrimitives();
		PublicPrimitives p5 = new PublicPrimitives();
		PublicPrimitives p6 = new PublicPrimitives();

		SimpleNested n = new SimpleNested();
		n.p1 = p1;
		n.p2 = p2;
		n.p3 = p3;

		try {
			byte[] b = JavaStruct.pack(n);

			SimpleNested n2 = new SimpleNested();
			n2.p1 = p4;
			n2.p2 = p5;
			n2.p3 = p6;

			assertFalse(n2.equals(n));
			JavaStruct.unpack(n2, b);
			assertTrue(n2.equals(n));

		} catch (StructException e) {
			e.printStackTrace();
			fail();
		}
	}

}
