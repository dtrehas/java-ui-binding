package com.beirtipol.binding.core.aop;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import com.beirtipol.binding.core.pcs.BindableMethod;
import com.beirtipol.binding.core.pcs.IBindable;
import com.beirtipol.binding.core.pcs.NestedPropertyChangeSupport;

public aspect PropertySupportAspect {
	/**
	 * Weave any class which is tagged with @BindableClass with NestedPropertyChangeSupport
	 */
	declare parents: @BindableClass * implements PropertySupport, IBindable;

	NestedPropertyChangeSupport PropertySupport.support = new NestedPropertyChangeSupport(this);

	public interface PropertySupport {
		public void addPropertyChangeListener(PropertyChangeListener listener);

		public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

		public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

		public void removePropertyChangeListener(PropertyChangeListener listener);

		public boolean hasListeners(String propertyName);

		public void firePropertyChange(Object b, String property, Object oldval, Object newval);
	}
	
	public PropertyChangeSupport PropertySupport.changeSupport() {
		return support;
	}

	public void PropertySupport.addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void PropertySupport.addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}

	public void PropertySupport.removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		support.removePropertyChangeListener(propertyName, listener);
	}

	public void PropertySupport.removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	public boolean PropertySupport.hasListeners(String propertyName) {
		return support.hasListeners(propertyName);
	}

	pointcut callSetter(PropertySupport b) : 
		call( @BindableMethod * *(..) ) 
		&& target( b );

	void around(PropertySupport b) : callSetter( b )  
	{
		Field propertyField = getField(thisJoinPointStaticPart.getSignature());
		try {
			propertyField.setAccessible(true);
			Object oldValue = propertyField.get(b);
			proceed(b);
			Object newValue = propertyField.get(b);
			((PropertySupport) b).firePropertyChange(b, propertyField.getName(), oldValue, newValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Field getField(Signature signature) {
		Field field = null;

		try {
			MethodSignature ms = (MethodSignature) signature;
		    Method m = ms.getMethod();
			BindableMethod annotation = m.getAnnotation(BindableMethod.class);
			field = signature.getDeclaringType().getDeclaredField(annotation.fieldName());
		} catch (NoSuchFieldException nsfe) {
			nsfe.printStackTrace();
		}
		return field;

	}

	public void PropertySupport.firePropertyChange(Object b, String property, Object oldval, Object newval) {
		support.firePropertyChange(property, oldval, newval);

	}
}