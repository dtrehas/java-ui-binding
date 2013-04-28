package com.beirtipol.binding.swt.demo;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.tree.AbstractTreeBinder;
import com.beirtipol.binding.core.binders.tree.ITreeBinder;
import com.beirtipol.binding.core.tree.ITreeNode;
import com.beirtipol.binding.core.tree.ITypedTreeNode;
import com.beirtipol.binding.core.tree.reflection.BasicReflectionTreeNodeIDContext;
import com.beirtipol.binding.core.tree.reflection.TreeNodeFactory;
import com.beirtipol.binding.swt.SWTAbstractPresentableComposite;
import com.beirtipol.binding.swt.SWTTreeDelegate;
import com.beirtipol.binding.swt.demo.SWTTreeBinderDemo.Presenter;

public class SWTTreeBinderDemo extends SWTAbstractPresentableComposite<Presenter> {

	private TreeViewer treeViewer;
	private SWTTreeDelegate delegate;

	public SWTTreeBinderDemo(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doCreateControls() {
		setLayout(new FillLayout());
		treeViewer = new TreeViewer(this);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);

		{
			delegate = new SWTTreeDelegate(treeViewer);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.treeBinder.setDelegate(delegate);
	}

	class Presenter extends AbstractPresenter {

		private final ITreeBinder treeBinder = new AbstractTreeBinder() {
			@Override
			public List<ITreeNode> getRoots() {
				return Arrays.asList(new ITreeNode[] { treeNode });
			}

			@Override
			public void select(ITreeNode... node) {
				// TODO Auto-generated
				// method stub
			}

			@Override
			public void execute(ITreeNode selectedNode) {
				// TODO Auto-generated
				// method stub

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
		SWTTreeBinderDemo comp = new SWTTreeBinderDemo(shell, SWT.NONE);
		ITypedTreeNode treeNode = TreeNodeFactory.createTreeNode(null, "Display", display, new BasicReflectionTreeNodeIDContext());
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
