package struct;

import mina.MinaStructPacker;
import junit.framework.TestCase;

public class TestPrimitives2 extends TestCase {

	public void testPublicPrimitives() {
		PublicPrimitives pp = new PublicPrimitives();
		pp.setParams();
		try {
			MinaStructPacker so = new MinaStructPacker();
			byte[] b = so.pack(pp);
			PublicPrimitives pp2 = new PublicPrimitives();
			assertFalse(pp.equals(pp2));
			JavaStruct.unpack(pp2, b);
			assertTrue(pp.equals(pp2));
		} catch (StructException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testPrivatePrimitives() {
		PrivatePrimitives pp = new PrivatePrimitives();
		pp.setParams();
		try {
			MinaStructPacker so = new MinaStructPacker();
			byte[] b = so.pack(pp);	
			PrivatePrimitives pp2 = new PrivatePrimitives();
			assertFalse(pp.equals(pp2));
			JavaStruct.unpack(pp2, b);
			assertTrue(pp.equals(pp2));
		} catch (StructException e) {
			e.printStackTrace();
			fail();
		}
	}

}
