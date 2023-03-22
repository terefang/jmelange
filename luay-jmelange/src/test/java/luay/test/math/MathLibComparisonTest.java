package luay.test.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import luay.vm.LuaError;
import luay.vm.LuaValue;
import luay.vm.lib.jse.JsePlatform;

class MathLibComparisonTest {

	private LuaValue j2se;
	private boolean  supportedOnJ2me;

	@BeforeEach
	protected void setUp() {
		j2se = JsePlatform.standardGlobals().get("math");
		supportedOnJ2me = true;
	}

	@Test
	void testMathDPow() {
		assertEquals(1, j2sepow(2, 0), 0);
		assertEquals(2, j2sepow(2, 1), 0);
		assertEquals(8, j2sepow(2, 3), 0);
		assertEquals(-8, j2sepow(-2, 3), 0);
		assertEquals(1/8., j2sepow(2, -3), 0);
		assertEquals(-1/8., j2sepow(-2, -3), 0);
		assertEquals(16, j2sepow(256, .5), 0);
		assertEquals(4, j2sepow(256, .25), 0);
		assertEquals(64, j2sepow(256, .75), 0);
		assertEquals(1./16, j2sepow(256, -.5), 0);
		assertEquals(1./4, j2sepow(256, -.25), 0);
		assertEquals(1./64, j2sepow(256, -.75), 0);
		assertEquals(Double.NaN, j2sepow(-256, .5), 0);
		assertEquals(1, j2sepow(.5, 0), 0);
		assertEquals(.5, j2sepow(.5, 1), 0);
		assertEquals(.125, j2sepow(.5, 3), 0);
		assertEquals(2, j2sepow(.5, -1), 0);
		assertEquals(8, j2sepow(.5, -3), 0);
		assertEquals(1, j2sepow(0.0625, 0), 0);
		assertEquals(0.00048828125, j2sepow(0.0625, 2.75), 0);
	}

	private double j2sepow(double x, double y) {
		return j2se.get("pow").call(LuaValue.valueOf(x), LuaValue.valueOf(y)).todouble();
	}

	@Test
	void testAbs() {
		tryMathOp("abs", 23.45);
		tryMathOp("abs", -23.45);
	}

	@Test
	void testCos() {
		tryTrigOps("cos");
	}

	@Test
	void testCosh() {
		supportedOnJ2me = false;
		tryTrigOps("cosh");
	}

	@Test
	void testDeg() {
		tryTrigOps("deg");
	}

	@Test
	void testExp() {
		tryMathOp("exp", 0);
		tryMathOp("exp", 0.1);
		tryMathOp("exp", .9);
		tryMathOp("exp", 1.);
		tryMathOp("exp", 9);
		tryMathOp("exp", -.1);
		tryMathOp("exp", -.9);
		tryMathOp("exp", -1.);
		tryMathOp("exp", -9);
	}

	@Test
	void testLog() {
		supportedOnJ2me = false;
		tryMathOp("log", 0.1);
		tryMathOp("log", .9);
		tryMathOp("log", 1.);
		tryMathOp("log", 9);
		tryMathOp("log", -.1);
		tryMathOp("log", -.9);
		tryMathOp("log", -1.);
		tryMathOp("log", -9);
	}

	@Test
	void testRad() {
		tryMathOp("rad", 0);
		tryMathOp("rad", 0.1);
		tryMathOp("rad", .9);
		tryMathOp("rad", 1.);
		tryMathOp("rad", 9);
		tryMathOp("rad", 10);
		tryMathOp("rad", 100);
		tryMathOp("rad", -.1);
		tryMathOp("rad", -.9);
		tryMathOp("rad", -1.);
		tryMathOp("rad", -9);
		tryMathOp("rad", -10);
		tryMathOp("rad", -100);
	}

	@Test
	void testSin() {
		tryTrigOps("sin");
	}

	@Test
	void testSinh() {
		supportedOnJ2me = false;
		tryTrigOps("sinh");
	}

	@Test
	void testSqrt() {
		tryMathOp("sqrt", 0);
		tryMathOp("sqrt", 0.1);
		tryMathOp("sqrt", .9);
		tryMathOp("sqrt", 1.);
		tryMathOp("sqrt", 9);
		tryMathOp("sqrt", 10);
		tryMathOp("sqrt", 100);
	}

	@Test
	void testTan() {
		tryTrigOps("tan");
	}

	@Test
	void testTanh() {
		supportedOnJ2me = false;
		tryTrigOps("tanh");
	}

	@Test
	void testAtan2() {
		supportedOnJ2me = false;
		tryDoubleOps("atan2", false);
	}

	@Test
	void testFmod() {
		tryDoubleOps("fmod", false);
	}

	@Test
	void testPow() {
		tryDoubleOps("pow", true);
	}

	private void tryDoubleOps(String op, boolean positiveOnly) {
		// y>0, x>0
		tryMathOp(op, 0.1, 4.0);
		tryMathOp(op, .9, 4.0);
		tryMathOp(op, 1., 4.0);
		tryMathOp(op, 9, 4.0);
		tryMathOp(op, 10, 4.0);
		tryMathOp(op, 100, 4.0);

		// y>0, x<0
		tryMathOp(op, 0.1, -4.0);
		tryMathOp(op, .9, -4.0);
		tryMathOp(op, 1., -4.0);
		tryMathOp(op, 9, -4.0);
		tryMathOp(op, 10, -4.0);
		tryMathOp(op, 100, -4.0);

		if (!positiveOnly) {
			// y<0, x>0
			tryMathOp(op, -0.1, 4.0);
			tryMathOp(op, -.9, 4.0);
			tryMathOp(op, -1., 4.0);
			tryMathOp(op, -9, 4.0);
			tryMathOp(op, -10, 4.0);
			tryMathOp(op, -100, 4.0);

			// y<0, x<0
			tryMathOp(op, -0.1, -4.0);
			tryMathOp(op, -.9, -4.0);
			tryMathOp(op, -1., -4.0);
			tryMathOp(op, -9, -4.0);
			tryMathOp(op, -10, -4.0);
			tryMathOp(op, -100, -4.0);
		}

		// degenerate cases
		tryMathOp(op, 0, 1);
		tryMathOp(op, 1, 0);
		tryMathOp(op, -1, 0);
		tryMathOp(op, 0, -1);
		tryMathOp(op, 0, 0);
	}

	private void tryTrigOps(String op) {
		tryMathOp(op, 0);
		tryMathOp(op, Math.PI/8);
		tryMathOp(op, Math.PI*7/8);
		tryMathOp(op, Math.PI*8/8);
		tryMathOp(op, Math.PI*9/8);
		tryMathOp(op, -Math.PI/8);
		tryMathOp(op, -Math.PI*7/8);
		tryMathOp(op, -Math.PI*8/8);
		tryMathOp(op, -Math.PI*9/8);
	}

	private void tryMathOp(String op, double x) {
		try {
			double expected = j2se.get(op).call(LuaValue.valueOf(x)).todouble();
		} catch (LuaError lee) {
				throw lee;
		}
	}

	private void tryMathOp(String op, double a, double b) {
		try {
			double expected = j2se.get(op).call(LuaValue.valueOf(a), LuaValue.valueOf(b)).todouble();
		} catch (LuaError lee) {
				throw lee;
		}
	}
}
