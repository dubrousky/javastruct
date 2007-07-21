package struct;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for struct");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestPrimitiveArrays.class);
		suite.addTestSuite(TestPrimitives.class);
		suite.addTestSuite(TestSimpleNestedStructs.class);
		suite.addTestSuite(TestLengthMarker.class);
		suite.addTestSuite(PerfTests.class);
		// $JUnit-END$
		return suite;
	}

}
