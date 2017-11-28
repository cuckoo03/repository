package com.java.inaction.ch08;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Ch08MainTest {
	@Test
	public void testMoveRighthBy() {
		Point p1 = new Point(5, 5);
		Point p2 = p1.moveRightBy(10);

		assertEquals(15, p2.getX());
		assertEquals(5, p2.getY());
	}

	@Test
	public void testComparingTwoPoints() {
		Point p1 = new Point(10, 15);
		Point p2 = new Point(10, 20);
		int result = Point.compareBy.compare(p1, p2);
		assertEquals(-1, result);
	}

	@Test
	public void tesetMoveAllPointsRightBy() {
		List<Point> points = Arrays.asList(new Point(5, 5), new Point(10, 5));
		List<Point> expectedPoints = Arrays.asList(new Point(15, 5), new Point(
				20, 5));
		List<Point> newPoints = Point.moveAllPointsRightBy(points, 10);
		assertEquals(expectedPoints, newPoints);
	}
}
