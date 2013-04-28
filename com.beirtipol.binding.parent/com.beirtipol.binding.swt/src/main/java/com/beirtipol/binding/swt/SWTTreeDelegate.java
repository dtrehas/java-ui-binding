package com.beirtipol.binding.swt;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;

import com.beirtipol.binding.core.binders.tree.ITreeBinder;
import com.beirtipol.binding.core.tree.ITreeNode;

public class SWTTreeDelegate extends SWTBaseTreeDelegate<ITreeBinder> {

	private MyLabelProvider labelProvider;

	public SWTTreeDelegate(TreeViewer viewer) {
		super(viewer);
	}

	@Override
	public void addSelectionListener(ITreeBinder binder) {
	}

	@Override
	public void free() {
	}

	private class MyLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof ITreeNode) {
				return ((ITreeNode) element).getName();
			}
			return super.getText(element);
		}
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new MyLabelProvider();
		}
		return labelProvider;
	}
}
