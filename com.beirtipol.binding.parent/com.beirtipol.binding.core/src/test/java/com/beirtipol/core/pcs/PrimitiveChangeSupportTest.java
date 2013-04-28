package com.beirtipol.core.pcs;

import java.beans.PropertyChangeSupport;

import org.junit.Test;

import com.beirtipol.binding.core.pcs.BindableMethod;
import com.beirtipol.binding.core.pcs.BindableMethod.Type;
import com.beirtipol.binding.core.pcs.IBindable;
import com.beirtipol.binding.core.pcs.NestedPropertyChangeSupport;
import com.beirtipol.binding.core.pcs.PCSReflectiveTest;

public class PrimitiveChangeSupportTest implements IBindable {
	private PropertyChangeSupport	changeSupport;

	public static final String		FLD_NAME	= "name";
	private long					name;

	@Override
	public PropertyChangeSupport changeSupport() {
		if (changeSupport == null) {
			changeSupport = new NestedPropertyChangeSupport(this);
		}
		return changeSupport;
	}

	@BindableMethod(fieldName = FLD_NAME)
	public void setName(long name) {
		long oldValue = this.name;
		this.name = name;
		changeSupport().firePropertyChange(FLD_NAME, oldValue, this.name);
	}

	@BindableMethod(fieldName = FLD_NAME, type = Type.GET)
	public long getName() {
		return name;
	}

	@Test
	public void testMe() throws Exception {
		PrimitiveChangeSupportTest me = new PrimitiveChangeSupportTest();
		me.setName(2l);
		PCSReflectiveTest.testObject(me);
	}
}
