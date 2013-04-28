package com.beirtipol.binding.core.tree;

import java.util.List;

/**
 * Based on a nice best-practice example from
 * http://www.eclipsezone.com/eclipse/forums/t53983.html
 * 
 * @author O041484
 */
public interface ITreeNode {
	public String getName();

	public List<ITreeNode> getChildren();

	public boolean hasChildren();

	public ITreeNode getParent();
}