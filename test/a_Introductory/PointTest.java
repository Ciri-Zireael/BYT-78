package a_Introductory;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PointTest {
	Point p1, p2, p3;
	
	@Before
	public void setUp() throws Exception {
		p1 = new Point(7, 9);
		p2 = new Point(-3, -30);
		p3 = new Point(-10, 3);
	}

	/*
	* The annotations @Test were missing so I added them
	*
	* Plus since I'm working in intelliJ I had to cast the second argument of the expressions to int
	* because otherwise it was yelling at me that it was an ambiguous method call
	* */
	@Test
	public void testAdd() {
		Point res1 = p1.add(p2);
		Point res2 = p1.add(p3);
		
		assertEquals(4, (int)res1.x);
		assertEquals(-21, (int)res1.y);
		assertEquals(-3, (int)res2.x);
		/*
		* There was assertEquals(12, (int)res2.x); so I changed it to res.y so that the test passes
		* */
		assertEquals(12, (int)res2.y);
	}

	@Test
	public void testSub() {
		Point res1 = p1.sub(p2);
		Point res2 = p1.sub(p3);

		/*
		* Before all the assertions here were just copied over directly
		* */
		assertEquals(10, (int)res1.x);
		assertEquals(39, (int)res1.y);
		assertEquals(17, (int)res2.x);
		assertEquals(6, (int)res2.y);
	}

}
