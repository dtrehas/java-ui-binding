package com.beirtipol.binding.core.tree;

public abstract class AbstractTreeNode implements ITreeNode {

	private final ITreeNode parent;

	public AbstractTreeNode(ITreeNode parent) {
		this.parent = parent;
	}

	@Override
	public ITreeNode getParent() {
		return parent;
	}
}