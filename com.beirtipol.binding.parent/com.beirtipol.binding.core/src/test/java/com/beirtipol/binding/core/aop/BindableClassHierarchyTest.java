package com.beirtipol.binding.core.aop;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.beirtipol.binding.core.pcs.BindableMethod;
import com.beirtipol.binding.core.pcs.BindableMethod.Type;
import com.beirtipol.binding.core.pcs.NestedPropertyChangeEvent;
import com.beirtipol.binding.core.pcs.NestedPropertyChangeListener;
import com.beirtipol.binding.core.pcs.PCSReflectiveTest;

public class BindableClassHierarchyTest {

	@Test
	public void proveAOPWorksLikeExplicitBinding() {
		Parent parent = new Parent();
		Child child = new Child();
		parent.setChild(child);
		child.setName("Some Name");
		PCSReflectiveTest.testObject(parent);
	}

	@Test
	public void testNestedListening() {
		Parent parent = new Parent();
		Child child = new Child();
		parent.setChild(child);

		final List<PropertyChangeEvent> parentEvents = new ArrayList<PropertyChangeEvent>();
		parent.addPropertyChangeListener(new NestedPropertyChangeListener(Parent.FLD_CHILD, Child.FLD_NAME) {

			@Override
			public void propertyChange(NestedPropertyChangeEvent evt) {
				parentEvents.add(evt);
			}
		});

		final List<PropertyChangeEvent> childEvents = new ArrayList<PropertyChangeEvent>();
		child.addPropertyChangeListener(new NestedPropertyChangeListener(Child.FLD_NAME) {

			@Override
			public void propertyChange(NestedPropertyChangeEvent evt) {
				childEvents.add(evt);
			}
		});

		final List<PropertyChangeEvent> childStandardEvents = new ArrayList<PropertyChangeEvent>();
		child.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				childStandardEvents.add(evt);
			}
		});

		child.setName("New Name");
		Assert.assertEquals(1, parentEvents.size());
		Assert.assertEquals(1, childEvents.size());
		Assert.assertEquals(1, childStandardEvents.size());
	}

	@BindableClass
	private class Parent {
		public static final String FLD_CHILD = "child";
		private Child child;

		@BindableMethod(fieldName = FLD_CHILD)
		public void setChild(Child child) {
			this.child = child;
		}

		@BindableMethod(fieldName = FLD_CHILD, type = Type.GET)
		public Child getChild() {
			return child;
		}
	}

	@BindableClass
	private class Child {
		public static final String FLD_NAME = "name";
		private String name;

		@BindableMethod(fieldName = FLD_NAME)
		public void setName(String name) {
			this.name = name;
		}

		@BindableMethod(fieldName = FLD_NAME, type = Type.GET)
		public String getName() {
			return name;
		}
	}
}
