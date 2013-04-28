package com.beirtipol.binding.core.pcs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.beirtipol.binding.core.util.StringStack;

public abstract class NestedPropertyChangeListener implements PropertyChangeListener {
	private final StringStack path;

	public NestedPropertyChangeListener(String... path) {
		this(new StringStack(path));
	}

	public NestedPropertyChangeListener(StringStack path) {
		this.path = path;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt instanceof NestedPropertyChangeEvent) {
			boolean matches = path == null || path.size() == 0;
			if (!matches) {
				StringStack eventPath = ((NestedPropertyChangeEvent) evt).getPropertyPath();
				matches = eventPath.startsWith(path);
			}
			if (matches) {
				propertyChange((NestedPropertyChangeEvent) evt);
			}
		}

	}

	public abstract void propertyChange(NestedPropertyChangeEvent evt);

}
