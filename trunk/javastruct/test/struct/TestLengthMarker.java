package struct;

import junit.framework.TestCase;

public class TestLengthMarker extends TestCase {

	public void testLengthMarker() {
		LengthMarkers m = new LengthMarkers();
		m.init(100);
		m.fill();

		try {
			byte[] buf = JavaStruct.pack(m);
			LengthMarkers m2 = new LengthMarkers();
			m2.bufferLength = 100;
			assertFalse(m.equals(m2));

			JavaStruct.unpack(m2, buf);
			assertTrue(m.equals(m2));
			assertEquals(100, m2.buffer.length);

		} catch (StructException e) {
			e.printStackTrace();
		}

	}
}
