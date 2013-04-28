package com.beirtipol.binding.core.tree;

import java.util.List;

public class StatefulTreeNode extends AbstractTreeNode {

	private final ITreeNode delegate;
	private boolean enabled;

	public StatefulTreeNode(ITreeNode delegate) {
		super(delegate.getParent());
		this.delegate = delegate;
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public boolean hasChildren() {
		return delegate.hasChildren();
	}

	@Override
	public ITreeNode getParent() {
		return delegate.getParent();
	}

	@Override
	public List<ITreeNode> getChildren() {
		return delegate.getChildren();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}