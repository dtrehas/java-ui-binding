package com.beirtipol.binding.core.pcs;

import java.beans.PropertyChangeSupport;

import org.junit.Test;

import com.beirtipol.binding.core.pcs.IBindable;
import com.beirtipol.binding.core.pcs.NestedPropertyChangeSupport;

public class NoBindableMethodsTest implements IBindable {
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

	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		changeSupport().firePropertyChange(FLD_NAME, oldValue, this.name);
	}

	public String getName() {
		return name;
	}

	@Test(expected = Exception.class)
	public void testMe() throws Exception {
		NoBindableMethodsTest me = new NoBindableMethodsTest();
		me.setName("me");
		PCSReflectiveTest.testObject(me);
	}
}
