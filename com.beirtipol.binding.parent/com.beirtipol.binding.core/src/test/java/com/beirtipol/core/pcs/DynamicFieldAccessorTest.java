package com.beirtipol.core.pcs;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Test;

import com.beirtipol.binding.core.pcs.FormattedFieldAccessor;
import com.beirtipol.binding.core.util.ItemBinder;
import com.beirtipol.binding.core.util.StringStack;

public class DynamicFieldAccessorTest {
	@Test
	public void testAccessSingleDepthField() {
		StringStack field = new StringStack(WorkingChangeSupportTest.FLD_NAME);
		FormattedFieldAccessor<String> accessor = new FormattedFieldAccessor<String>(new ItemBinder<String>() {
			@Override
			public String convert(String value) {
				return value;
			}

			@Override
			public Color getBackground(String value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Color getForeground(String value) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		WorkingChangeSupportTest data = new WorkingChangeSupportTest();

		String newName = "New Name";
		accessor.setValue(data, field, newName);
		Assert.assertEquals(newName, accessor.getValue(data, field));
	}

	@Test
	public void testAccessNestedField() {
		StringStack field = new StringStack(WorkingNestedChangeSupportTest.FLD_INNER_OBJ, WorkingChangeSupportTest.FLD_NAME);
		FormattedFieldAccessor<String> accessor = new FormattedFieldAccessor<String>(new ItemBinder<String>() {

			@Override
			public String convert(String value) {
				return value;
			}

			@Override
			public Color getBackground(String value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Color getForeground(String value) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		WorkingNestedChangeSupportTest data = new WorkingNestedChangeSupportTest();
		data.setInnerObj(new WorkingChangeSupportTest());

		String newName = "New Name";
		accessor.setValue(data, field, newName);
		Assert.assertEquals(newName, accessor.getValue(data, field));
	}
}
