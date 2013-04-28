package com.beirtipol.binding.swt;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.binders.widget.IListBinder;
import com.beirtipol.binding.core.delegates.IListDelegate;
import com.beirtipol.binding.core.util.ItemBinder;
import com.beirtipol.binding.swt.util.ColorConverter;

public class SWTListDelegate implements IListDelegate {
	private ListViewer list;
	@SuppressWarnings("rawtypes")
	private ItemBinder converter;

	public SWTListDelegate(ListViewer list) {
		if (list == null) {
			throw new IllegalArgumentException("List cannot be null");
		}
		this.list = list;
		list.setContentProvider(new ArrayContentProvider());
		list.setLabelProvider(new LabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(Object element) {
				return converter.convert(element);
			}
		});
	}

	@Override
	public void setItemBinder(ItemBinder<?> converter) {
		this.converter = converter;
	}

	private List<?> getSelection() {
		StructuredSelection selection = (StructuredSelection) list
				.getSelection();
		return Arrays.asList(selection.toArray());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addKeyListener(final IListBinder binder) {
		list.getList().addKeyListener(new KeyAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.keyCode;
				if (code == SWT.CR || code == SWT.KEYPAD_CR) {
					binder.execute(getSelection().get(0));
				}
			}
		});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addMouseListener(final IListBinder binder) {
		list.getList().addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				binder.execute(getSelection().get(0));
			}
		});
	}

	@Override
	public void addSelectionListener(final IListBinder<?> binder) {
		list.getList().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				binder.setSelectedItems(getSelection());
			}
		});
	}

	@Override
	public void setAllItems(final List<?> items) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				list.setInput(items.toArray(new Object[0]));
			}
		});
	}

	@Override
	public void setBackground(final Color background) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				list.getList()
						.setBackground(ColorConverter.convert(background));
			}
		});
	}

	@Override
	public void setEnabled(final boolean enabled) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				list.getList().setEnabled(enabled);
			}
		});
	}

	@Override
	public void setForeground(final Color textColour) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				list.getList()
						.setForeground(ColorConverter.convert(textColour));
			}
		});
	}

	@Override
	public void setSelectedItems(final List<?> items) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				list.setSelection(new StructuredSelection(items));
			}
		});
	}

	@Override
	public void setToolTip(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				list.getList().setToolTipText(text);
			}
		});
	}

	@Override
	public void setVisible(final boolean visible) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				list.getList().setVisible(visible);
			}
		});
	}

	@Override
	public void free() {
		if (list != null && !list.getList().isDisposed()) {
			list.getList().dispose();
		}
		list = null;
	}

}
