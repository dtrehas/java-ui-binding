package com.beirtipol.binding.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.beirtipol.binding.core.binders.tree.ITreeBinder;
import com.beirtipol.binding.core.delegates.ITreeDelegate;
import com.beirtipol.binding.core.tree.ITreeNode;
import com.beirtipol.binding.core.tree.StatefulTreeNode;

public class SWTMenuDelegate implements ITreeDelegate<ITreeBinder> {
	private ITreeBinder binder;
	private final Button button;
	private final Menu newMenu;
	private List<ITreeNode> roots = new ArrayList<ITreeNode>();

	public SWTMenuDelegate(final Button button) {
		this.button = button;
		newMenu = new Menu(button.getShell(), SWT.POP_UP);
		newMenu.addListener(SWT.Show, new Listener() {
			@Override
			public void handleEvent(Event event) {
				for (MenuItem item : newMenu.getItems()) {
					item.dispose();
				}
				if (roots != null) {
					for (final ITreeNode node : roots) {
						createMenuItem(node, newMenu);
					}
				}
			}
		});
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newMenu.setVisible(true);
			}
		});
	}

	@Override
	public void setRoots(final List<ITreeNode> roots) {
		this.roots = roots;
	}

	public void setText(final String string) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.setText(string);
				}
			}
		});
	}

	public void setToolTip(final String tip) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.setToolTipText(tip);
				}
			}
		});
	}

	private void createMenuItem(final ITreeNode node, Menu parent) {
		MenuItem result = null;
		if (node.hasChildren()) {
			result = new MenuItem(parent, SWT.CASCADE);
			Menu itemMenu = new Menu(result);
			for (ITreeNode childNode : node.getChildren()) {
				createMenuItem(childNode, itemMenu);
			}
			result.setMenu(itemMenu);
		} else {
			result = new MenuItem(parent, SWT.PUSH);
			if (node instanceof StatefulTreeNode) {
				result.setEnabled(((StatefulTreeNode) node).isEnabled());
			}
			result.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (binder != null) {
						binder.select(node);
					}
				}
			});
		}
		if (result != null) {
			result.setText(node.getName());
		}
	}

	/**
	 * menu is built dynamically. We cannot add the listener yet. @see
	 * {@link SWTMenuDelegate#createMenuItem(ITreeNode, Menu)}
	 */
	@Override
	public void addSelectionListener(ITreeBinder binder) {
		this.binder = binder;
	}

	@Override
	public void free() {
		this.binder = null;
	}
}
