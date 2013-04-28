package com.beirtipol.binding.core.tree.reflection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.beirtipol.binding.core.tree.ITreeNode;
import com.beirtipol.binding.core.tree.ITypedTreeNode;

public class TreeNodeFactory {
	private static List<Class<?>> simpleTreeNodeList = new ArrayList<Class<?>>();
	static {
		simpleTreeNodeList.add(Integer.class);
		simpleTreeNodeList.add(Double.class);
		simpleTreeNodeList.add(BigDecimal.class);
		simpleTreeNodeList.add(String.class);
		simpleTreeNodeList.add(Enum.class);
		simpleTreeNodeList.add(Boolean.class);
	}

	public static ITypedTreeNode createTreeNode(ITreeNode parent, String name,
			Object model, BasicReflectionTreeNodeIDContext idContext) {
		ITypedTreeNode result = null;
		if (model == null) {
			result = new SimpleTreeNode(parent, name, model, idContext);
		} else {
			for (Class<?> clazz : simpleTreeNodeList) {
				if (clazz.isAssignableFrom(model.getClass())) {
					result = new SimpleTreeNode(parent, name, model, idContext);
					break;
				}
			}
			if (result == null) {
				if (List.class.isAssignableFrom(model.getClass())) {
					result = new ListTreeNode(parent, name, (List<?>) model,
							idContext);
				} else if (Set.class.isAssignableFrom(model.getClass())) {
					result = new SetTreeNode(parent, name, (Set<?>) model,
							idContext);
				} else if (Map.class.isAssignableFrom(model.getClass())) {
					result = new MapTreeNode(parent, name, (Map<?, ?>) model,
							idContext);
				} else if (model.getClass().isArray()) {
					result = new GenericArrayTreeNode(parent, name, model,
							idContext);
				} else {
					result = new BasicReflectionTreeNode(parent, name, model,
							idContext);
				}
			}
		}
		return result;
	}
}
