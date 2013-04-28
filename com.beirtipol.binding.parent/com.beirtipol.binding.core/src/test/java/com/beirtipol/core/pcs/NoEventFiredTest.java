package com.beirtipol.core.pcs;

import java.beans.PropertyChangeSupport;

import org.junit.Test;

import com.beirtipol.binding.core.pcs.BindableMethod;
import com.beirtipol.binding.core.pcs.IBindable;
import com.beirtipol.binding.core.pcs.NestedPropertyChangeSupport;
import com.beirtipol.binding.core.pcs.PCSReflectiveTest;

public class NoEventFiredTest implements IBindable {
	private PropertyChangeSupport	changeSupport;

	public static final String		FLD_NAME	= "name";
	private String					name;

	@Override
	public PropertyChangeSupport changeSupport() {
		if (changeSupport == null) {
			changeSupport = new NestedPropertyChangeSupport(this);
		}
		return changeSupport;
	}

	@BindableMethod(fieldName = FLD_NAME)
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Test(expected = Exception.class)
	public void testMe() throws Exception {
		NoEventFiredTest me = new NoEventFiredTest();
		me.setName("me");
		PCSReflectiveTest.testObject(me);
	}
}
