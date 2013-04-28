package com.beirtipol.binding.swt;

import java.awt.Color;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.core.delegates.IButtonDelegate;
import com.beirtipol.binding.swt.util.ColorConverter;

public class SWTButtonDelegate implements IButtonDelegate {
	private Button button;

	public SWTButtonDelegate(Button button) {
		this.button = button;

	}

	@Override
	public void setImagePath(final String path) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					// button.setImage(SWTResourceManager.getImage(path));
				}
			}
		});
	}

	@Override
	public void addPressedListener(final IButtonBinder binder) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							pressed(binder);
						}
					});
				}
			}
		});
	}

	protected void pressed(IButtonBinder binder) {
		binder.pressed();
	}

	public void setImage(final Image image) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.setImage(image);
				}
			}
		});
	}

	@Override
	public void setText(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// SWT Error if text is null
				if (button != null && !button.isDisposed() && text != null) {
					button.setText(text);
				}
			}
		});
	}

	@Override
	public void setToolTip(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (button != null && !button.isDisposed()) {
					button.setToolTipText(text);
				}
			}
		});
	}

	public boolean isEnabled() {
		return button.isEnabled();
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
		button.dispose();
	}
}