package com.beirtipol.core.pcs;

import java.beans.PropertyChangeSupport;

import org.junit.Test;

import com.beirtipol.binding.core.pcs.BindableMethod;
import com.beirtipol.binding.core.pcs.BindableMethod.Type;
import com.beirtipol.binding.core.pcs.IBindable;
import com.beirtipol.binding.core.pcs.NestedPropertyChangeSupport;

public class WorkingChangeSupportTest implements IBindable {
	private PropertyChangeSupport changeSupport;

	public static final String FLD_NAME = "name";
	private String name;

	@Override
	public PropertyChangeSupport changeSupport() {
		if (changeSupport == null) {
			changeSupport = new NestedPropertyChangeSupport(this);
		}
		return changeSupport;
	}

	@BindableMethod(fieldName = FLD_NAME)
	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		changeSupport().firePropertyChange(FLD_NAME, oldValue, this.name);
	}

	@BindableMethod(fieldName = FLD_NAME, type = Type.GET)
	public String getName() {
		return name;
	}

	@Test
	public void testMe() throws Exception {
		WorkingChangeSupportTest me = new WorkingChangeSupportTest();
		me.setName("me");
		PCSReflectiveTest.testObject(me);
	}
}
