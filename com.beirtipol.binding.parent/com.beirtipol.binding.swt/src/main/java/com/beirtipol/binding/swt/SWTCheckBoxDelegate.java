package com.beirtipol.binding.swt;

import java.awt.Color;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.binders.widget.ICheckBoxBinder;
import com.beirtipol.binding.core.delegates.ICheckBoxDelegate;
import com.beirtipol.binding.core.delegates.IDelegate;
import com.beirtipol.binding.swt.util.ColorConverter;

public class SWTCheckBoxDelegate implements ICheckBoxDelegate, ISWTDelegate {
	private Button button;
	private IBasicBinder<? extends IDelegate> basicBinder;

	public SWTCheckBoxDelegate(Button button) {
		this.button = button;

		this.button.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				SWTCheckBoxDelegate.this.button = null;
				if (basicBinder != null) {
					basicBinder.setDelegate(null);
				}
			}
		});
	}

	@Override
	public Control getControl() {
		return button;
	}

	@Override
	public void addPressedListener(final ICheckBoxBinder binder) {
		if (button != null && !button.isDisposed()) {
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					binder.setCheckedIntoModel(button.getSelection());
				}
			});
		}
	}

	@Override
	public void setChecked(final boolean checked) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.setSelection(checked);
				}
			}
		});
	}

	@Override
	public void setText(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.setText(text);
				}
			}
		});
	}

	public void setBinder(IBasicBinder<? extends IDelegate> binder) {
		this.basicBinder = binder;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.setEnabled(enabled);
				}
			}
		});

	}

	@Override
	public void setVisible(final boolean visible) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.setVisible(visible);
				}
			}
		});
	}

	@Override
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

	@Override
	public void setBackground(final Color color) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.setBackground(ColorConverter.convert(color));
				}
			}
		});
	}

	@Override
	public void setForeground(final Color color) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.setForeground(ColorConverter.convert(color));
				}
			}
		});
	}

	@Override
	public void free() {
		if (button != null) {
			button.dispose();
		}

		basicBinder = null;
	}
}