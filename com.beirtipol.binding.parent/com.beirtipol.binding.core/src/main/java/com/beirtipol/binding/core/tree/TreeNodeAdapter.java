package com.beirtipol.binding.core.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeNodeAdapter implements ITreeNode {
	protected ITreeNode parent;
	protected List<ITreeNode> children;

	public TreeNodeAdapter() {
		this(null);
	}

	public TreeNodeAdapter(ITreeNode parent) {
		this.parent = parent;
	}

	@Override
	public boolean hasChildren() {
		return getChildren().size() > 0;
	}

	@Override
	public ITreeNode getParent() {
		return parent;
	}

	@Override
	public List<ITreeNode> getChildren() {
		if (children != null) {
			return children;
		}

		children = new ArrayList<ITreeNode>();
		createChildren(children);

		return children;
	}

	/**
	 * subclasses should override this method to add the child nodes
	 */
	protected void createChildren(List<ITreeNode> children) {
	}
}
