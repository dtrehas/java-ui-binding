package com.beirtipol.binding.swing;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTree;

import com.beirtipol.binding.core.binders.tree.ITreeBinder;
import com.beirtipol.binding.core.delegates.ITreeDelegate;
import com.beirtipol.binding.core.tree.ITreeNode;
import com.beirtipol.binding.core.tree.TreeNodeAdapter;

public class SwingTreeDelegate implements ITreeDelegate<ITreeBinder> {
	private final JXTree tree;
	private ITreeBinder binder;

	public SwingTreeDelegate(JXTree tree) {
		this.tree = tree;
		setupListening();
		setupRendering();
	}

	@SuppressWarnings("serial")
	private void setupRendering() {
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				return super.getTreeCellRendererComponent(tree, ((ITreeNode) value).getName(), selected, expanded, leaf, row, hasFocus);
			}
		});
	}

	private void setupListening() {

	}

	@Override
	public void addSelectionListener(final ITreeBinder binder) {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (binder != null) {
					TreePath[] selectionPaths = tree.getSelectionPaths();
					List<ITreeNode> selectedNodes = new ArrayList<ITreeNode>();
					for (TreePath path : selectionPaths) {
						selectedNodes.add((ITreeNode) path.getLastPathComponent());
					}
					if (selectedNodes.size() > 0) {
						binder.select(selectedNodes.toArray(new ITreeNode[0]));
					}
				}
			}
		});

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					executeSelectedNode();
				}
			}
		});
		tree.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					executeSelectedNode();
				}
			}
		});
	}

	@Override
	public void setRoots(List<ITreeNode> roots) {
		tree.setModel(createTreeModel(roots));
	}

	private TreeModel createTreeModel(final List<ITreeNode> roots) {
		return new TreeModel() {
			@Override
			public void valueForPathChanged(TreePath path, Object newValue) {
			}

			@Override
			public void addTreeModelListener(TreeModelListener l) {
			}

			@Override
			public void removeTreeModelListener(TreeModelListener l) {
			}

			@Override
			public boolean isLeaf(Object node) {
				return !((ITreeNode) node).hasChildren();
			}

			@Override
			public Object getRoot() {
				if (roots.size() == 1) {
					return roots.get(0);
				} else {
					return new TreeNodeAdapter() {

						@Override
						public String getName() {
							return "Root";
						}

						@Override
						protected void createChildren(List<ITreeNode> children) {
							children.addAll(roots);
						}
					};
				}
			}

			@Override
			public int getIndexOfChild(Object parent, Object child) {
				ITreeNode casted = (ITreeNode) parent;
				if (casted.hasChildren()) {
					return casted.getChildren().indexOf(child);
				}
				return 0;
			}

			@Override
			public int getChildCount(Object parent) {
				ITreeNode casted = (ITreeNode) parent;
				if (casted.hasChildren()) {
					return casted.getChildren().size();
				}
				return 0;
			}

			@Override
			public Object getChild(Object parent, int index) {
				ITreeNode casted = (ITreeNode) parent;
				if (casted.hasChildren()) {
					return casted.getChildren().get(index);
				}
				return null;
			}
		};
	}

	private void executeSelectedNode() {
		ITreeNode selectedNode = (ITreeNode) SwingTreeDelegate.this.tree.getLastSelectedPathComponent();

		/**
		 * Plausible for fast mouse navigation picking up double clicks
		 */
		if (selectedNode != null) {
			binder.execute(selectedNode);
		}
	}

	@Override
	public void free() {
	}
}