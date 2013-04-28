package com.beirtipol.binding.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.beirtipol.binding.core.binders.tree.ITreeBinder;
import com.beirtipol.binding.core.delegates.ITreeDelegate;
import com.beirtipol.binding.core.tree.ITreeNode;
import com.beirtipol.binding.swing.util.MenuScroller;

public class SwingMenuDelegate implements ITreeDelegate<ITreeBinder> {
	private JPopupMenu popup;
	private JButton parentButton;
	private ITreeBinder binder;
	private Map<String, ImageIcon> imageCache = new HashMap<String, ImageIcon>();

	@SuppressWarnings("rawtypes")
	public SwingMenuDelegate(JPopupMenu menu, final JButton parentButton) {
		this.popup = menu;
		this.parentButton = parentButton;
		popup.setLightWeightPopupEnabled(false);
		parentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Crap Crap Crap, need to fix this.
				if (binder != null) {
					binder.updateUI();
				}
				popup.show(parentButton, 0, parentButton.getHeight());
			}
		});
		try {
			Class cls = Class.forName("javax.swing.PopupFactory");
			Field field = cls.getDeclaredField("forceHeavyWeightPopupKey");
			field.setAccessible(true);
			popup.putClientProperty(field.get(null), Boolean.TRUE);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// What a beauty!
		// http://tips4java.wordpress.com/2009/02/01/menu-scroller/
		MenuScroller.setScrollerFor(popup, 20, 25, 1, 1);
	}

	@Override
	public void setRoots(List<ITreeNode> menuItems) {
		popup.removeAll();
		for (final ITreeNode node : menuItems) {
			popup.add(createMenuItem(node));
		}
	}

	private JMenuItem createMenuItem(final ITreeNode node) {
		JMenuItem result;
		if (node.hasChildren()) {
			result = new JMenu(node.getName());
			for (ITreeNode childNode : node.getChildren()) {
				result.add(createMenuItem(childNode));
			}
		} else {
			result = new JMenuItem(node.getName());
			result.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (binder != null) {
						binder.select(node);
					}
				}
			});
		}
		return result;
	}

	@Override
	public void free() {
		popup = null;
		parentButton = null;
		binder = null;
		imageCache = null;
	}

	@Override
	public void addSelectionListener(ITreeBinder binder) {
		// TODO Auto-generated method stub
	}
}
