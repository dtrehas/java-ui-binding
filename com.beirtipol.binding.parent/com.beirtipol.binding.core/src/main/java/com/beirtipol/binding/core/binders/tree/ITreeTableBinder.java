package com.beirtipol.binding.core.binders.tree;

import java.util.List;

import com.beirtipol.binding.core.tree.ITreeNode;

public interface ITreeTableBinder extends ITreeBinder {
	List<String> getColumnHeaders();

	/**
	 * @param node
	 * @param col
	 * @return a String of the text for this column of this node. It is
	 *         forseeable that we would want to support editable trees, in which
	 *         case we would return a binder and have the tree delegate decide
	 *         what to render on it. That's a little overkill for the moment.
	 */
	String getColumnFor(ITreeNode node, int col);

	int getInitialColumnWidthFor(int col);
}
