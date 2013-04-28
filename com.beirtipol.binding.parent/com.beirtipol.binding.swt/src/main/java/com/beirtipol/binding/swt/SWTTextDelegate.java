package com.beirtipol.binding.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.binders.widget.ITextBinder;
import com.beirtipol.binding.core.delegates.IDelegate;
import com.beirtipol.binding.core.delegates.ITextDelegate;
import com.beirtipol.binding.swt.util.ColorConverter;

public class SWTTextDelegate implements ITextDelegate, ISWTDelegate {
	private Text text;
	private IBasicBinder<? extends IDelegate> basicBinder;
	boolean isMouseClicked = false;

	public SWTTextDelegate(final Text text) {
		if (text == null) {
			throw new IllegalArgumentException("Text cannot be null");
		}
		this.text = text;
		this.text.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				SWTTextDelegate.this.text = null;
				if (basicBinder != null) {
					basicBinder.setDelegate(null);
				}
			}
		});
		this.text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent event) {
				// Ctrl + a = select all
				if (event.stateMask == SWT.CTRL) {
					if (event.keyCode == 97) {
						text.setSelection(0, text.getCharCount());
					}
				}

			}
		});
		this.text.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				isMouseClicked = true;
			}

			@Override
			public void mouseDown(MouseEvent e) {
				if (text.isEnabled()) {
					if (!isMouseClicked) {
						isMouseClicked = true;
						text.setFocus();
						text.setSelection(0, text.getText().length());
					}
				}
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});

	}

	@Override
	public Control getControl() {
		return text;
	}

	@Override
	public void setText(final String string) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (text != null && !text.isDisposed()) {
					text.setText(string);
					if (text.isFocusControl()) {
						text.setSelection(0, text.getText().length());
					}
				}
			}
		});
	}

	@Override
	public void setToolTip(final String tip) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (text != null && !text.isDisposed()) {
					text.setToolTipText(tip);
				}
			}
		});
	}

	@Override
	public void addModifyListener(final ITextBinder binder) {
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				performListenerAction(binder);
			}
		});
	}

	@Override
	public void addFocusListener(final ITextBinder binder) {
		if (text != null && !text.isDisposed()) {
			text.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					super.focusGained(e);
					isMouseClicked = false;
					if (text != null && !text.isDisposed()) {
						text.setSelection(0, text.getText().length());
					}
				}

				@Override
				public void focusLost(FocusEvent e) {
					performListenerAction(binder);
				}
			});
		}
	}

	@Override
	public void addTraverseListener(final ITextBinder binder) {
		if (text != null && !text.isDisposed()) {
			text.addTraverseListener(new TraverseListener() {
				@Override
				public void keyTraversed(TraverseEvent e) {
					// We don't want the value to be set into the model every
					// time the user presses
					// an arrow key to navigate through the text field
					if (e.character == SWT.TAB || e.character == SWT.CR) {
						performListenerAction(binder);
					}
				}
			});
		}
	}

	@Override
	public void setEnabled(final boolean enabled) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (text != null && !text.isDisposed()) {
					text.setEditable(enabled);
					Event event = new Event();
					event.widget = text;
					int listener = enabled ? SWT.Activate : SWT.Deactivate;
					text.notifyListeners(listener, event);
				}
			}
		});
	}

	@Override
	public void setBackground(final java.awt.Color color) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (text != null && !text.isDisposed()) {
					text.setBackground(ColorConverter.convert(color));
				}
			}
		});
	}

	@Override
	public void setForeground(final java.awt.Color color) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (text != null && !text.isDisposed()) {
					text.setForeground(ColorConverter.convert(color));
				}
			}
		});
	}

	public void setBinder(IBasicBinder<? extends IDelegate> binder) {
		this.basicBinder = binder;
	}

	protected void performListenerAction(final ITextBinder binder) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				String uiText = text.getText();
				String modelText = binder.getTextFromModel();
				if (uiText != null && !uiText.equals(modelText)) {
					binder.setTextIntoModel(uiText);
				}
				binder.updateUI();
			}
		});
	}

	@Override
	public void setVisible(final boolean visible) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (text == null || text.isDisposed()) {
					return;
				}
				text.setVisible(visible);
			}
		});
	}

	@Override
	public void free() {
		if (text != null) {
			text.dispose();
			text = null;
		}
		basicBinder = null;
	}
}