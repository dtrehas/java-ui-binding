package com.beirtipol.binding.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeColumn;

import com.beirtipol.binding.core.binders.tree.ITreeTableBinder;
import com.beirtipol.binding.core.tree.ITreeNode;

public class SWTTreeTableDelegate extends SWTBaseTreeDelegate<ITreeTableBinder> {

	private ITreeTableBinder binder;
	private MyLabelProvider labelProvider;

	public SWTTreeTableDelegate(TreeViewer viewer) {
		super(viewer);
	}

	private ITreeNode[] getSelection() {
		StructuredSelection selection = (StructuredSelection) getViewer()
				.getSelection();
		List<ITreeNode> nodes = new ArrayList<ITreeNode>();
		for (Object o : selection.toArray()) {
			nodes.add((ITreeNode) o);
		}
		return nodes.toArray(new ITreeNode[0]);
	}

	@Override
	public void addSelectionListener(final ITreeTableBinder binder) {
		this.binder = binder;
		int idx = 0;
		for (String s : binder.getColumnHeaders()) {
			TreeColumn trclmnName = new TreeColumn(getViewer().getTree(),
					SWT.NONE);
			trclmnName.setWidth(binder.getInitialColumnWidthFor(idx++));
			trclmnName.setText(s);
		}
		getViewer().getTree().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				binder.select(getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = new MyLabelProvider();
		}
		return labelProvider;
	}

	private class MyLabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ITreeNode) {
				return binder.getColumnFor((ITreeNode) element, columnIndex);
			}
			return null;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}
	}
}
