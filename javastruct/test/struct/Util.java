package struct;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import mina.MinaStructPacker;


public class Util {
	public static void testPerf(Object o, int iters) {
		try {
			TimeTracker.startClock("s");
			byte[] b = null;
			for (int i = 0; i < iters; i++) {
				b = JavaStruct.pack(o);
			}
			System.out.println("(" + o.getClass().getName()
					+ ") Packing performance:  "
					+ TimeTracker.getItemCountPerSecond("s", iters));
			System.out.println("Size: " + b.length);
			TimeTracker.stopClock("s");

			TimeTracker.startClock("s");
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			ObjectOutputStream oos = new ObjectOutputStream(bos);

			for (int i = 0; i < iters; i++) {
				oos.writeObject(o);
				oos.reset();
				b = bos.toByteArray();
				bos.reset();
			}
			System.out.println("(" + o.getClass().getName()
					+ ") Serialization performance:  "
					+ TimeTracker.getItemCountPerSecond("s", iters));
			System.out.println("Size: " + b.length);
			TimeTracker.stopClock("s");

		} catch (StructException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testPerf2(Object o, int iters) {
		try {
			TimeTracker.startClock("s");
			byte[] b = null;
			
			MinaStructPacker out = new MinaStructPacker();
			for (int i = 0; i < iters; i++) {
				b = out.pack(o);
				out.reset();
			}
			System.out.println("(" + o.getClass().getName()
					+ ") Packing performance:  "
					+ TimeTracker.getItemCountPerSecond("s", iters));
			System.out.println("Size: " + b.length);
			TimeTracker.stopClock("s");

			TimeTracker.startClock("s");
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			ObjectOutputStream oos = new ObjectOutputStream(bos);

			for (int i = 0; i < iters; i++) {
				oos.writeObject(o);
				oos.reset();
				b = bos.toByteArray();
				bos.reset();
			}
			System.out.println("(" + o.getClass().getName()
					+ ") Serialization performance:  "
					+ TimeTracker.getItemCountPerSecond("s", iters));
			System.out.println("Size: " + b.length);
			TimeTracker.stopClock("s");

		} catch (StructException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean arraysEqual(byte[] a1, byte[] a2) {
		if ((a1 == null && a2 != null) || (a1 != null && a2 == null))
			return false;
		if (a1.length != a2.length)
			return false;
		if (a1 == null && a2 == null)
			return true;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i])
				return false;
		}
		return true;
	}

	public static boolean arraysEqual(short[] a1, short[] a2) {
		if ((a1 == null && a2 != null) || (a1 != null && a2 == null))
			return false;
		if (a1.length != a2.length)
			return false;
		if (a1 == null && a2 == null)
			return true;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i])
				return false;
		}
		return true;
	}

	public static boolean arraysEqual(char[] a1, char[] a2) {
		if ((a1 == null && a2 != null) || (a1 != null && a2 == null))
			return false;
		if (a1.length != a2.length)
			return false;
		if (a1 == null && a2 == null)
			return true;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i])
				return false;
		}
		return true;
	}

	public static boolean arraysEqual(int[] a1, int[] a2) {
		if ((a1 == null && a2 != null) || (a1 != null && a2 == null))
			return false;
		if (a1.length != a2.length)
			return false;
		if (a1 == null && a2 == null)
			return true;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i])
				return false;
		}
		return true;
	}

	public static boolean arraysEqual(long[] a1, long[] a2) {
		if ((a1 == null && a2 != null) || (a1 != null && a2 == null))
			return false;
		if (a1.length != a2.length)
			return false;
		if (a1 == null && a2 == null)
			return true;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i])
				return false;
		}
		return true;
	}

	public static boolean arraysEqual(float[] a1, float[] a2) {
		if ((a1 == null && a2 != null) || (a1 != null && a2 == null))
			return false;
		if (a1.length != a2.length)
			return false;
		if (a1 == null && a2 == null)
			return true;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i])
				return false;
		}
		return true;
	}

	public static boolean arraysEqual(double[] a1, double[] a2) {
		if ((a1 == null && a2 != null) || (a1 != null && a2 == null))
			return false;
		if (a1.length != a2.length)
			return false;
		if (a1 == null && a2 == null)
			return true;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i])
				return false;
		}
		return true;
	}

	public static boolean arraysEqual(Object[] a1, Object[] a2) {
		if ((a1 == null && a2 != null) || (a1 != null && a2 == null))
			return false;
		if (a1.length != a2.length)
			return false;
		if (a1 == null && a2 == null)
			return true;
		for (int i = 0; i < a1.length; i++) {
			if (!a1[i].equals(a2[i]))
				return false;
		}
		return true;
	}

	private Util() {
	}

}
