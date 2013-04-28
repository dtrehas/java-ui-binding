package com.beirtipol.binding.core.tree.reflection;

import java.util.List;

import com.beirtipol.binding.core.tree.ITreeNode;

public class ListTreeNode extends BasicReflectionTreeNode {
	private final List<?> model;

	public ListTreeNode(ITreeNode parent, String name, List<?> model,
			BasicReflectionTreeNodeIDContext idContext) {
		super(parent, name, model, idContext);
		this.model = model;
	}

	@Override
	public void createChildren(List<ITreeNode> children) {
		for (int i = 0; i < model.size(); i++) {
			String nodeName = "[" + i + "]";
			children.add(TreeNodeFactory.createTreeNode(this, nodeName,
					model.get(i), idContext));
		}
	}

	@Override
	protected String getObjectTypeString() {
		return super.getObjectTypeString() + "[" + model.size() + "]";
	}

}