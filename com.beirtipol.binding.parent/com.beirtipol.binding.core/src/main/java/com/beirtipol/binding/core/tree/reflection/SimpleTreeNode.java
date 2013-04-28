package com.beirtipol.binding.core.tree.reflection;

import java.util.List;

import com.beirtipol.binding.core.tree.ITreeNode;

public class SimpleTreeNode extends BasicReflectionTreeNode {
	public SimpleTreeNode(ITreeNode parent, String name, Object model, BasicReflectionTreeNodeIDContext idContext) {
		super(parent, name, model, idContext);
	}

	@Override
	public void createChildren(List<ITreeNode> children) {
	}

	@Override
	public boolean hasChildren() {
		return false;
	}
}