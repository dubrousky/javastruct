package struct;

import junit.framework.TestCase;

public class TestPrimitiveArrays extends TestCase {

	public void testPublicPrimitiveArrays() {
		PublicPrimitiveArrays ppa = new PublicPrimitiveArrays();
		ppa.init(10);
		ppa.setAsc(10);
		try {
			byte[] b = JavaStruct.pack(ppa);
			PublicPrimitiveArrays ppa2 = new PublicPrimitiveArrays();
			ppa2.init(10);
			ppa2.setDesc(10);
			// first check they are not equal
			assertFalse(Util.arraysEqual(ppa.b, ppa2.b));
			assertFalse(Util.arraysEqual(ppa.c, ppa2.c));
			assertFalse(Util.arraysEqual(ppa.s, ppa2.s));
			assertFalse(Util.arraysEqual(ppa.i, ppa2.i));
			assertFalse(Util.arraysEqual(ppa.lo, ppa2.lo));
			assertFalse(Util.arraysEqual(ppa.f, ppa2.f));
			assertFalse(Util.arraysEqual(ppa.d, ppa2.d));

			JavaStruct.unpack(ppa2, b);
			// Now they should be equal
			assertTrue(Util.arraysEqual(ppa.b, ppa2.b));
			assertTrue(Util.arraysEqual(ppa.c, ppa2.c));
			assertTrue(Util.arraysEqual(ppa.s, ppa2.s));
			assertTrue(Util.arraysEqual(ppa.i, ppa2.i));
			assertTrue(Util.arraysEqual(ppa.lo, ppa2.lo));
			assertTrue(Util.arraysEqual(ppa.f, ppa2.f));
			assertTrue(Util.arraysEqual(ppa.d, ppa2.d));
		} catch (StructException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testPrivatePrimitiveArrays() {
		PrivatePrimitiveArrays ppa = new PrivatePrimitiveArrays();
		ppa.init(10);
		ppa.setAsc(10);
		try {
			byte[] b = JavaStruct.pack(ppa);
			PrivatePrimitiveArrays ppa2 = new PrivatePrimitiveArrays();
			ppa2.init(10);
			ppa2.setDesc(10);
			// first check they are not equal
			assertFalse(Util.arraysEqual(ppa.getB(), ppa2.getB()));
			assertFalse(Util.arraysEqual(ppa.getC(), ppa2.getC()));
			assertFalse(Util.arraysEqual(ppa.getS(), ppa2.getS()));
			assertFalse(Util.arraysEqual(ppa.getI(), ppa2.getI()));
			assertFalse(Util.arraysEqual(ppa.getLo(), ppa2.getLo()));
			assertFalse(Util.arraysEqual(ppa.getF(), ppa2.getF()));
			assertFalse(Util.arraysEqual(ppa.getD(), ppa2.getD()));

			JavaStruct.unpack(ppa2, b);
			// Now they should be equal
			assertTrue(Util.arraysEqual(ppa.getB(), ppa2.getB()));
			assertTrue(Util.arraysEqual(ppa.getC(), ppa2.getC()));
			assertTrue(Util.arraysEqual(ppa.getS(), ppa2.getS()));
			assertTrue(Util.arraysEqual(ppa.getI(), ppa2.getI()));
			assertTrue(Util.arraysEqual(ppa.getLo(), ppa2.getLo()));
			assertTrue(Util.arraysEqual(ppa.getF(), ppa2.getF()));
			assertTrue(Util.arraysEqual(ppa.getD(), ppa2.getD()));
		} catch (StructException e) {
			e.printStackTrace();
			fail();
		}
	}

}
