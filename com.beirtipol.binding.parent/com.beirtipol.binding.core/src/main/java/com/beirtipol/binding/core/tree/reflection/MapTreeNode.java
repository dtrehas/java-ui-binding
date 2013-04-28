package com.beirtipol.binding.core.tree.reflection;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.beirtipol.binding.core.tree.ITreeNode;

public class MapTreeNode extends BasicReflectionTreeNode {
	private final Map<?, ?> model;

	public MapTreeNode(ITreeNode parent, String name, Map<?, ?> model,
			BasicReflectionTreeNodeIDContext idContext) {
		super(parent, name, model, idContext);
		this.model = model;
	}

	@Override
	public void createChildren(List<ITreeNode> children) {
		int i = 0;
		for (Entry<?, ?> entry : model.entrySet()) {
			String nodeName = "[" + i++ + "]";
			children.add(new MapEntryTreeNode(this, nodeName, entry, idContext));
		}
	}

	@Override
	protected String getObjectTypeString() {
		return super.getObjectTypeString() + "[" + model.size() + "]";
	}
}