package com.beirtipol.binding.core.binders.tree;

import java.util.List;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.ITreeDelegate;
import com.beirtipol.binding.core.tree.ITreeNode;

@SuppressWarnings("rawtypes")
public interface ITreeBinder extends IBasicBinder<ITreeDelegate> {
	public List<ITreeNode> getRoots();

	void select(ITreeNode... node);

	public void execute(ITreeNode selectedNode);

}
