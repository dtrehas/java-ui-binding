package com.beirtipol.binding.core.tree.reflection;

import java.util.List;
import java.util.Set;

import com.beirtipol.binding.core.tree.ITreeNode;

public class SetTreeNode extends BasicReflectionTreeNode {
	private final Set<?> model;

	public SetTreeNode(ITreeNode parent, String name, Set<?> model, BasicReflectionTreeNodeIDContext idContext) {
		super(parent, name, model, idContext);
		this.model = model;
	}

	@Override
	public void createChildren(List<ITreeNode> children) {
		String nodeName = "[e]";
		for (Object element : model) {
			children.add(TreeNodeFactory.createTreeNode(this, nodeName, element, idContext));
		}
	}

	@Override
	protected String getObjectTypeString() {
		return super.getObjectTypeString() + "[" + model.size() + "]";
	}
}