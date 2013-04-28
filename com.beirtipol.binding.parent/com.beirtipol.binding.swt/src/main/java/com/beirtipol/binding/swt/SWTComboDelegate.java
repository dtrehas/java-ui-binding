package com.beirtipol.binding.swt;

import java.util.Arrays;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.binders.widget.IComboBinder;
import com.beirtipol.binding.core.delegates.IComboDelegate;
import com.beirtipol.binding.swt.util.ColorConverter;
import com.beirtipol.binding.swt.util.SWTSelectionUtils;

public class SWTComboDelegate implements IComboDelegate, ISWTDelegate {
	private ComboViewer combo;
	private IComboBinder basicBinder;
	private final AutoCompleteField autoCompleteField;
	protected String[] currentProposals;

	public SWTComboDelegate(Combo combo) {
		this(new ComboViewer(combo));
	}

	public SWTComboDelegate(ComboViewer combo) {
		this.combo = combo;
		combo.setContentProvider(new ArrayContentProvider());
		combo.getCombo().setVisibleItemCount(25);
		autoCompleteField = new AutoCompleteField(combo.getControl(), new ComboContentAdapter(), new String[] {});
		this.combo.getCombo().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				SWTComboDelegate.this.combo = null;
				if (basicBinder != null) {
					basicBinder.setDelegate(null);
				}
			}
		});
	}

	@Override
	public void setSelected(final Object o) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (o == null) {
					return;
				}
				if (combo != null && !combo.getCombo().isDisposed()) {
					updateAvailableItems();
					if (combo.getInput() == null) {
						combo.setInput(new Object[] { o });
					}
					Object selectedItem = SWTSelectionUtils.getSingleSelection(combo.getSelection());
					if (ObjectUtils.equals(selectedItem, o)) {
						return;
					}
					combo.setSelection(new StructuredSelection(o));
				}
			}
		});
	}

	@Override
	public void addTraverseListener(final IComboBinder binder) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (combo != null && !combo.getCombo().isDisposed()) {
					combo.getCombo().addTraverseListener(new TraverseListener() {
						@Override
						public void keyTraversed(TraverseEvent e) {
							if (isReadOnlyStyle()) {
								// If the combo is in read only mode we
								// only
								// want to listen for
								// selection events (otherwise combos
								// like the
								// Add combo in the
								// trade asset view will get multiple
								// events)
								return;
							}
							binder.setSelectedItem(SWTSelectionUtils.getSingleSelection(combo.getSelection()));
						}
					});
				}
			}
		});
	}

	@Override
	public void addSelectionListener(final IComboBinder binder) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (combo != null && !combo.getCombo().isDisposed()) {
					combo.getCombo().addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							Object selection = SWTSelectionUtils.getSingleSelection(combo.getSelection());
							binder.setSelectedItem(selection);

							/**
							 * Ultra-hack 1: The AutoCompleteField gets
							 * activated as soon as the user selects something.
							 * If the proposals contains something like
							 * ['Aaa','Aaab'] and the user selects 'Aaa' from
							 * the dropdown, the autoComplete detects entry of
							 * 'Aaa' and pops up the proposal for 'Aaab'. By
							 * clearing the proposals and then resetting them on
							 * the next run of the event thread, we stop this.
							 */
							autoCompleteField.setProposals(new String[0]);
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									autoCompleteField.setProposals(currentProposals);
								}
							});
						}
					});
				}
			}
		});
	}

	@Override
	public void addFocusListener(final IComboBinder binder) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (combo != null && !combo.getCombo().isDisposed()) {
					combo.getCombo().addFocusListener(new FocusAdapter() {
						@Override
						public void focusGained(FocusEvent e) {
							updateAvailableItems();
						}

						@SuppressWarnings("unchecked")
						@Override
						public void focusLost(FocusEvent e) {
							Object selection = SWTSelectionUtils.getSingleSelection(combo.getSelection());
							if (selection == null) {
								try {
									for (Object item : binder.getAvailableItems()) {
										if (binder.getItemBinder().convert(item).equalsIgnoreCase(combo.getCombo().getText())) {
											selection = item;
											break;
										}
									}
								} catch (Exception e1) {
									return;
								}
							}
							binder.setSelectedItem(selection);
						}
					});
				}
			}
		});
	}

	@Override
	public void setBackground(final java.awt.Color color) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (combo != null && !combo.getCombo().isDisposed()) {
					combo.getCombo().setBackground(ColorConverter.convert(color));
				}
			}
		});
	}

	@Override
	public void setForeground(final java.awt.Color color) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (combo != null && !combo.getCombo().isDisposed()) {
					combo.getCombo().setForeground(ColorConverter.convert(color));
				}
			}
		});
	}

	@Override
	public Control getControl() {
		return combo.getCombo();
	}

	@Override
	public void setBinder(IComboBinder binder) {
		this.basicBinder = binder;
		combo.setLabelProvider(new LabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(Object element) {
				return basicBinder.getItemBinder().convert(element);
			}
		});
	}

	@Override
	public void setEnabled(final boolean enabled) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (combo != null && !combo.getCombo().isDisposed()) {
					combo.getCombo().setEnabled(enabled);
				}
			}
		});
	}

	@Override
	public void setVisible(final boolean visible) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (combo == null || combo.getCombo().isDisposed()) {
					return;
				}
				combo.getCombo().setVisible(visible);
			}
		});
	}

	@Override
	public void setToolTip(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (combo != null && !combo.getCombo().isDisposed()) {
					combo.getCombo().setToolTipText(text);
				}
			}
		});
	}

	private boolean isReadOnlyStyle() {
		return (combo.getCombo().getStyle() & SWT.READ_ONLY) == SWT.READ_ONLY;
	}

	@Override
	public void free() {
		if (combo != null && combo.getCombo() != null) {
			combo.getCombo().dispose();
			combo = null;
		}
		basicBinder = null;
	}

	@SuppressWarnings("unchecked")
	protected void updateAvailableItems() {
		Object[] items = basicBinder.getAvailableItems();
		if (Arrays.equals(items, (Object[]) combo.getInput())) {
			return;
		}
		ISelection selection = combo.getSelection();
		combo.setInput(items);
		String[] proposals = new String[items.length];
		for (int i = 0; i < items.length; i++) {
			proposals[i] = basicBinder.getItemBinder().convert(items[i]);
		}
		combo.setSelection(selection);
		SWTComboDelegate.this.currentProposals = proposals;

		/**
		 * Ultra-hack 2: The first time the user focus' on the combo, the
		 * proposals are set and both the dropdown menu from the combo and the
		 * table from the AutoCompleteField appear. Running a little bit later
		 * on the display thread prevents this happening.
		 */
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				autoCompleteField.setProposals(currentProposals);
			}
		});
	}
}