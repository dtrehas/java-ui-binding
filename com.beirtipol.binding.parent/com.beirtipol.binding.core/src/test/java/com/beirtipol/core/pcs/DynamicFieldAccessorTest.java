package com.beirtipol.core.pcs;
//package com.jpmorgan.ib.rates.rcc.binding.core.pcs;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import com.jpmorgan.ib.rates.rcc.binding.core.util.ItemBinder;
//import com.jpmorgan.ib.rates.rcc.binding.core.util.StringStack;
//
//public class DynamicFieldAccessorTest {
//	@Test
//	public void testAccessSingleDepthField() {
//		StringStack field = new StringStack(WorkingChangeSupportTest.FLD_NAME);
//		FormattedFieldAccessor<String> accessor = new FormattedFieldAccessor<String>(new ItemBinder<String>() {
//			@Override
//			public String toString(String o) {
//				return o;
//			}
//
//			@Override
//			public String parse(String s) {
//				return s;
//			}
//		});
//		WorkingChangeSupportTest data = new WorkingChangeSupportTest();
//
//		String newName = "New Name";
//		accessor.setValue(data, field, newName);
//		Assert.assertEquals(newName, accessor.getValue(data, field));
//	}
//
//	@Test
//	public void testAccessNestedField() {
//		StringStack field = new StringStack(WorkingNestedChangeSupportTest.FLD_INNER_OBJ, WorkingChangeSupportTest.FLD_NAME);
//		FormattedFieldAccessor<String> accessor = new FormattedFieldAccessor<String>(new ItemBinder<String>() {
//			@Override
//			public String toString(String o) {
//				return o;
//			}
//
//			@Override
//			public String parse(String s) {
//				return s;
//			}
//		});
//		WorkingNestedChangeSupportTest data = new WorkingNestedChangeSupportTest();
//		data.setInnerObj(new WorkingChangeSupportTest());
//
//		String newName = "New Name";
//		accessor.setValue(data, field, newName);
//		Assert.assertEquals(newName, accessor.getValue(data, field));
//	}
// }
