package com.beirtipol.binding.core.tree.reflection;

import java.lang.reflect.Array;
import java.util.List;

import com.beirtipol.binding.core.tree.ITreeNode;

public class GenericArrayTreeNode extends BasicReflectionTreeNode {
	private final Object model;

	public GenericArrayTreeNode(ITreeNode parent, String name, Object model, BasicReflectionTreeNodeIDContext idContext) {
		super(parent, name, model, idContext);
		this.model = model;
		if (model != null && !model.getClass().isArray()) {
			throw new IllegalArgumentException("This class can only handle objects which are arrays.");
		}
	}

	@Override
	public void createChildren(List<ITreeNode> children) {
		if (model == null) {
			return;
		}
		for (int i = 0; i < Array.getLength(model); i++) {
			String nodeName = "[" + i + "]";
			children.add(TreeNodeFactory.createTreeNode(this, nodeName, Array.get(model, i), idContext));
		}
	}

	@Override
	protected String getObjectTypeString() {
		if (model == null) {
			return "[null]";
		}
		return model.getClass().getSimpleName().replaceAll("\\[\\]", "[" + Array.getLength(model) + "]");
	}

	@Override
	public String toString() {
		return getObjectTypeString();
	}

}
