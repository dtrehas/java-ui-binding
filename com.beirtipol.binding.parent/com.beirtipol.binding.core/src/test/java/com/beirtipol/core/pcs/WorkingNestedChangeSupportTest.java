package com.beirtipol.core.pcs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.beirtipol.binding.core.pcs.BindableMethod;
import com.beirtipol.binding.core.pcs.BindableMethod.Type;
import com.beirtipol.binding.core.pcs.IBindable;
import com.beirtipol.binding.core.pcs.NestedPropertyChangeEvent;
import com.beirtipol.binding.core.pcs.NestedPropertyChangeListener;
import com.beirtipol.binding.core.pcs.NestedPropertyChangeSupport;

public class WorkingNestedChangeSupportTest implements IBindable {
	private NestedPropertyChangeSupport	changeSupport;

	public static final String			FLD_INNER_OBJ	= "innerObj";
	private WorkingChangeSupportTest	innerObj;

	@Override
	public PropertyChangeSupport changeSupport() {
		if (changeSupport == null) {
			changeSupport = new NestedPropertyChangeSupport(this);
		}
		return changeSupport;
	}

	@BindableMethod(fieldName = FLD_INNER_OBJ)
	public void setInnerObj(WorkingChangeSupportTest innerObj) {
		WorkingChangeSupportTest oldValue = this.innerObj;
		this.innerObj = innerObj;
		changeSupport().firePropertyChange(FLD_INNER_OBJ, oldValue, this.innerObj);
	}

	@BindableMethod(fieldName = FLD_INNER_OBJ, type = Type.GET)
	public WorkingChangeSupportTest getInnerObj() {
		return innerObj;
	}

	@Test
	public void testMe() throws Exception {
		final List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();

		WorkingNestedChangeSupportTest me = new WorkingNestedChangeSupportTest();
		WorkingChangeSupportTest innerMe = new WorkingChangeSupportTest();
		innerMe.setName("MiniMe");

		// The inner object should have no listeners to begin with
		Assert.assertFalse(innerMe.changeSupport().hasListeners(null));

		// Setting the inner object should add a listener
		me.setInnerObj(innerMe);
		Assert.assertTrue(innerMe.changeSupport().hasListeners(null));

		// Field listener
		me.changeSupport().addPropertyChangeListener(WorkingNestedChangeSupportTest.FLD_INNER_OBJ, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				events.add(evt);
			}
		});

		// All fields listener
		me.changeSupport().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				events.add(evt);
			}
		});

		// Different field listener. This should not be fired
		me.changeSupport().addPropertyChangeListener("Dummy Field", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				events.add(evt);
			}
		});

		innerMe.setName("MaxiMe");
		Assert.assertEquals(2, events.size());

		PropertyChangeEvent event = events.get(0);
		Assert.assertTrue(event instanceof NestedPropertyChangeEvent);

		PropertyChangeEvent sourceEvent = ((NestedPropertyChangeEvent) event).getSourceEvent();
		Assert.assertEquals(WorkingChangeSupportTest.FLD_NAME, sourceEvent.getPropertyName());

		// Setting the inner object to null should remove any listeners we have
		// added
		me.setInnerObj(null);
		Assert.assertFalse(innerMe.changeSupport().hasListeners(null));
	}

	@Test
	public void testGetRootEvent() {
		WorkingNestedChangeSupportTest me = new WorkingNestedChangeSupportTest();
		WorkingChangeSupportTest innerMe = new WorkingChangeSupportTest();
		innerMe.setName("MiniMe");
		me.setInnerObj(innerMe);

		final NestedPropertyChangeEvent[] eventArray = new NestedPropertyChangeEvent[1];

		me.changeSupport().addPropertyChangeListener(new NestedPropertyChangeListener() {
			@Override
			public void propertyChange(NestedPropertyChangeEvent evt) {
				eventArray[0] = evt;
			}
		});

		innerMe.setName("New Name");

		Assert.assertNotNull(eventArray[0]);
		Assert.assertEquals(innerMe, eventArray[0].getRootEvent().getSource());
	}
}
