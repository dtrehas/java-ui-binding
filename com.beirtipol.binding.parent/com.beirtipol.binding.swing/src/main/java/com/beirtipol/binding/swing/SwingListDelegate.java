package com.beirtipol.binding.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.beirtipol.binding.core.binders.widget.IListBinder;
import com.beirtipol.binding.core.delegates.IListDelegate;
import com.beirtipol.binding.core.util.ItemBinder;

public class SwingListDelegate implements IListDelegate {
	private final JList list;
	private List<?> listDataModel;

	public SwingListDelegate(JList list) {
		if (list == null) {
			throw new IllegalArgumentException("List cannot be null");
		}
		this.list = list;
	}

	@Override
	public void addSelectionListener(final IListBinder binder) {
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					binder.execute(Arrays.asList(list.getSelectedValues()));
				}
			}
		});

		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					// We get an event for mouse down as well as mouse up. This
					// can cause multiple
					// fires on close running threads through some
					// not-necessarily threadsafe code.
					return;
				}
				binder.setSelectedItems(Arrays.asList(list.getSelectedValues()));
			}
		});

	}

	/*
	 * This method doesn't need to do anything here because a mouse listener has
	 * been added to the component in the addSelectionListener() method.
	 */
	@Override
	public void addMouseListener(IListBinder binder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addKeyListener(final IListBinder binder) {
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_ENTER) {
					binder.execute(Arrays.asList(list.getSelectedValues()));
				}
			}
		});
	}

	@Override
	public void setAllItems(final List<?> items) {
		this.listDataModel = items;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (items != null) {
					list.setListData(items.toArray());
				}
			}
		});
	}

	@Override
	public void setBackground(final Color background) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				list.setBackground(background);
			}
		});
	}

	@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
	@Override
	public void setItemBinder(final ItemBinder converter) {
		list.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				label.setText(converter.convert(value));
				Color background = converter.getBackground(value);
				Color foreground = converter.getForeground(value);
				if (background != null) {
					if (isSelected) {
						label.getBackground();
						background = label.getBackground().brighter();
					}
					label.setBackground(background);
				}
				if (foreground != null) {
					if (!isSelected) {
						label.setForeground(foreground);
					}
				}
				return label;
			}
		});
	}

	@Override
	public void setEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				list.setEnabled(enabled);
			}
		});
	}

	@Override
	public void setForeground(final Color textColour) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				list.setForeground(textColour);
			}
		});
	}

	@Override
	public void setSelectedItems(List<?> items) {
		if (items != null) {
			final int[] selection = new int[items.size()];
			for (int i = 0; i < selection.length; i++) {
				selection[i] = listDataModel.indexOf(items.get(i));
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					list.setSelectedIndices(selection);
				}
			});
		}
	}

	@Override
	public void setToolTip(String text) {
		list.setToolTipText(text);
	}

	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				list.setVisible(visible);
				if (list.getParent() != null && list.getParent().getParent() instanceof JScrollPane) {
					list.getParent().getParent().setVisible(visible);
				}
			}
		});
	}

	@Override
	public void free() {
		// TODO Auto-generated method stub

	}
}