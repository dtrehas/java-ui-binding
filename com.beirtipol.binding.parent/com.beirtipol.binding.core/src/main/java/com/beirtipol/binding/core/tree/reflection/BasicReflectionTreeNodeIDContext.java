package com.beirtipol.binding.core.tree.reflection;

import java.util.IdentityHashMap;

public class BasicReflectionTreeNodeIDContext {
	private final IdentityHashMap<Object, Long> idMap = new IdentityHashMap<Object, Long>();
	private long idCounter = 0;

	public long getId(Object model) {
		Long id = idMap.get(model);
		if (id != null) {
			return id;
		} else {
			idCounter++;
			idMap.put(model, idCounter);
			return idCounter;
		}
	}

	public boolean contains(Object model) {
		return idMap.containsKey(model);
	}
}
