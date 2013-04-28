package com.beirtipol.binding.core.delegates;

import java.util.List;

import com.beirtipol.binding.core.binders.tree.ITreeBinder;
import com.beirtipol.binding.core.tree.ITreeNode;

public interface ITreeDelegate<T extends ITreeBinder> extends IDelegate {
	void setRoots(List<ITreeNode> roots);

	void addSelectionListener(T binder);
}