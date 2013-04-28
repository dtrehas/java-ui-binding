package com.beirtipol.core.pcs;

import java.beans.PropertyChangeSupport;

import org.junit.Test;

import com.beirtipol.binding.core.pcs.BindableMethod;
import com.beirtipol.binding.core.pcs.IBindable;

public class IncorrectEventFiredTest implements IBindable {
	private PropertyChangeSupport changeSupport;

	public static final String FLD_NAME = "name";
	private String name;

	@Override
	public PropertyChangeSupport changeSupport() {
		if (changeSupport == null) {
			changeSupport = new PropertyChangeSupport(this);
		}
		return changeSupport;
	}

	@BindableMethod(fieldName = FLD_NAME)
	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		changeSupport().firePropertyChange("WrongFieldName", oldValue, this.name);
	}

	public String getName() {
		return name;
	}

	@Test(expected = Exception.class)
	public void testMe() throws Exception {
		IncorrectEventFiredTest me = new IncorrectEventFiredTest();
		me.setName("me");
		PCSReflectiveTest.testObject(me);
	}
}
