package com.beirtipol.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.beirtipol.binding.core.util.StringStack;

public class StringStackTest {
	@Test
	public void testEquality() {
		Assert.assertEquals(new StringStack("1", "2", "3"), new StringStack("1", "2", "3"));
		Assert.assertFalse(new StringStack("1", "2", "3").equals(new StringStack("2", "1", "3")));
	}

	@Test
	public void testStartsWith() {
		Assert.assertTrue(new StringStack("1", "2", "3").startsWith(new StringStack("1", "2")));
	}

	@Test
	public void testPushString() {
		StringStack expected = new StringStack("1", "2", "3");
		Assert.assertEquals(expected, new StringStack("2", "3").push("1"));
	}

	@Test
	public void testPushStack() {
		StringStack expected = new StringStack("1", "2", "3");
		Assert.assertEquals(expected, new StringStack("3").push(new StringStack("1", "2")));
	}

	@Test
	public void testDrop() {
		StringStack original = new StringStack("1", "2", "3");
		StringStack expected = new StringStack("2", "3");
		Assert.assertEquals(expected, original.drop());
	}

	@Test
	public void testDropToPath() {
		StringStack original = new StringStack("1", "2", "3");
		StringStack expected = new StringStack("3");
		Assert.assertEquals("Expect to drop to correct point in stack", expected, original.drop(new StringStack("1", "2")));
		Assert.assertEquals("Expect an empty stack as the prefix does not match", new StringStack(), original.drop(new StringStack("2", "2")));
	}
}
