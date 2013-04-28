package com.beirtipol.binding.swt.demo;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.tree.AbstractTreeTableBinder;
import com.beirtipol.binding.core.binders.tree.ITreeBinder;
import com.beirtipol.binding.core.tree.ITreeNode;
import com.beirtipol.binding.core.tree.ITypedTreeNode;
import com.beirtipol.binding.core.tree.reflection.BasicReflectionTreeNodeIDContext;
import com.beirtipol.binding.core.tree.reflection.TreeNodeFactory;
import com.beirtipol.binding.swt.SWTAbstractPresentableComposite;
import com.beirtipol.binding.swt.SWTTreeTableDelegate;
import com.beirtipol.binding.swt.demo.SWTTreeTableBinderDemo.Presenter;

public class SWTTreeTableBinderDemo extends
		SWTAbstractPresentableComposite<Presenter> {

	private TreeViewer treeViewer;
	private SWTTreeTableDelegate delegate;

	public SWTTreeTableBinderDemo(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doCreateControls() {
		setLayout(new FillLayout());
		treeViewer = new TreeViewer(this);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);

		{
			delegate = new SWTTreeTableDelegate(treeViewer);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.treeBinder.setDelegate(delegate);
	}

	class Presenter extends AbstractPresenter {

		private final ITreeBinder treeBinder = new AbstractTreeTableBinder() {

			@Override
			public List<ITreeNode> getRoots() {
				return Arrays.asList(new ITreeNode[] { treeNode });
			}

			@Override
			public List<String> getColumnHeaders() {
				return Arrays.asList(new String[] { "Name", "Type", "Value" });
			}

			@Override
			public String getColumnFor(ITreeNode node, int col) {
				switch (col) {
				case 0:
					return node.getName();
				case 1:
					return ((ITypedTreeNode) node).getType();
				case 2:
					return ObjectUtils.toString(((ITypedTreeNode) node)
							.getValue());
				default:
					return "[NULL]";
				}
			}

			@Override
			public void select(ITreeNode... node) {
			}

			@Override
			public void execute(ITreeNode selectedNode) {
				// TODO Auto-generated
				// method stub

			}

			@Override
			public int getInitialColumnWidthFor(int col) {
				switch (col) {
				case 0:
					return 200;
				case 1:
					return 150;
				case 2:
					return 150;
				}
				return 0;
			}

		};
		private final ITreeNode treeNode;

		public Presenter(ITreeNode treeNode) {
			this.treeNode = treeNode;
		}

		@Override
		public void free() {
		}
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT");
		shell.setLayout(new FillLayout());
		SWTTreeTableBinderDemo comp = new SWTTreeTableBinderDemo(shell,
				SWT.NONE);
		ITypedTreeNode treeNode = TreeNodeFactory.createTreeNode(null,
				"Display", display, new BasicReflectionTreeNodeIDContext());
		Presenter presenter = comp.new Presenter(treeNode);
		comp.setPresenter(presenter);
		shell.open();
		presenter.updateUI();

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}

		display.dispose();
	}
}
