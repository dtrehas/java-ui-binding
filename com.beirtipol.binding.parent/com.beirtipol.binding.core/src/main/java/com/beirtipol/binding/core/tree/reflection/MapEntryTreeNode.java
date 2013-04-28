package com.beirtipol.binding.core.tree.reflection;

import java.util.List;
import java.util.Map.Entry;

import com.beirtipol.binding.core.tree.ITreeNode;

public class MapEntryTreeNode extends BasicReflectionTreeNode {
	private final Entry<?, ?> model;

	public MapEntryTreeNode(ITreeNode parent, String name, Entry<?, ?> model, BasicReflectionTreeNodeIDContext idContext) {
		super(parent, name, model, idContext);
		this.model = model;
	}

	@Override
	public void createChildren(List<ITreeNode> children) {
		children.add(TreeNodeFactory.createTreeNode(this, "Key", model.getKey(), idContext));
		children.add(TreeNodeFactory.createTreeNode(this, "Value", model.getValue(), idContext));
	}
}