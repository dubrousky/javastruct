package struct;

import junit.framework.TestCase;
import struct.StructException;
import struct.StructPacker;
import struct.StructUnpacker;

public class TestPrimitives extends TestCase {
	
	public void testPublicPrimitives(){
			PublicPrimitives pp = new PublicPrimitives();
			pp.setParams();
			try {
				byte[] b = StructPacker.pack(pp);
				PublicPrimitives pp2 = new PublicPrimitives();
				assertFalse(pp.equals(pp2));
				StructUnpacker.unpack(pp2, b);
				assertTrue(pp.equals(pp2));
			} catch (StructException e) {
				e.printStackTrace();
				fail();
			}
	}

	public void testPrivatePrimitives(){
		PrivatePrimitives pp = new PrivatePrimitives();
		pp.setParams();
		try {
			byte[] b = StructPacker.pack(pp);
			PrivatePrimitives pp2 = new PrivatePrimitives();
			assertFalse(pp.equals(pp2));
			StructUnpacker.unpack(pp2, b);
			assertTrue(pp.equals(pp2));
		} catch (StructException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testPublicPrimitivesPerf(){
		Util.testPerf(new PublicPrimitives(), 500000);
	}

	public void testPrivatePrimitivesPerf(){
		Util.testPerf(new PrivatePrimitives(), 500000);
	}
	
}
	

