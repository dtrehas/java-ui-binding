package com.beirtipol.binding.swt;

import java.util.List;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import com.beirtipol.binding.core.binders.tree.ITreeBinder;
import com.beirtipol.binding.core.delegates.ITreeDelegate;
import com.beirtipol.binding.core.tree.ITreeNode;

public abstract class SWTBaseTreeDelegate<T extends ITreeBinder> implements
		ITreeDelegate<T> {

	private TreeViewer viewer;
	private final MyContentProvider contentProvider = new MyContentProvider();

	public SWTBaseTreeDelegate(TreeViewer viewer) {
		this.viewer = viewer;
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(getLabelProvider());
	}

	protected TreeViewer getViewer() {
		return viewer;
	}

	protected abstract IBaseLabelProvider getLabelProvider();

	@Override
	public void setRoots(List<ITreeNode> roots) {
		viewer.setInput(roots.toArray());
	}

	@Override
	public void free() {
		if (viewer != null) {
			viewer.setInput(null);
			if (viewer.getContentProvider() == contentProvider) {
				viewer.setContentProvider(null);
			}
			viewer = null;
		}
	}

	private class MyContentProvider implements ITreeContentProvider {
		@Override
		public Object[] getElements(Object arg0) {
			if (arg0 instanceof ITreeNode[]) {
				return (Object[]) arg0;
			} else {
				return getChildren(arg0);
			}
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		}

		@Override
		public Object[] getChildren(Object arg0) {
			if (arg0 instanceof ITreeNode) {
				return ((ITreeNode) arg0).getChildren().toArray();
			} else if (arg0 instanceof Object[]) {
				return (Object[]) arg0;
			}
			return null;
		}

		@Override
		public Object getParent(Object arg0) {
			if (arg0 instanceof ITreeNode) {
				return ((ITreeNode) arg0).getParent();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object arg0) {
			if (arg0 instanceof ITreeNode) {
				return ((ITreeNode) arg0).hasChildren();
			}
			return false;
		}
	}
}
