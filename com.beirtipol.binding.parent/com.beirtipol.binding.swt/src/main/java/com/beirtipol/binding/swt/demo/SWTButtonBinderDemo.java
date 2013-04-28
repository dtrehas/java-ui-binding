package com.beirtipol.binding.swt.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.widget.AbstractButtonBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.swt.SWTAbstractPresentableComposite;
import com.beirtipol.binding.swt.SWTButtonDelegate;
import com.beirtipol.binding.swt.demo.SWTButtonBinderDemo.Presenter;

public class SWTButtonBinderDemo extends
		SWTAbstractPresentableComposite<Presenter> {
	private Button btnCount;
	private SWTButtonDelegate btnCountDelegate;

	public SWTButtonBinderDemo(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doCreateControls() {
		setLayout(new GridLayout(1, false));

		// Widgets
		{
			btnCount = new Button(this, SWT.NONE);
			btnCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
		}
		// Delegates
		{
			btnCountDelegate = new SWTButtonDelegate(btnCount);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getCountBinder().setDelegate(btnCountDelegate);
	}

	static class Presenter extends AbstractPresenter {
		private IButtonBinder countBinder;
		private int count = 0;

		public IButtonBinder getCountBinder() {
			if (countBinder == null) {
				countBinder = new AbstractButtonBinder() {
					@Override
					public String getText() {
						return "" + count;
					}

					@Override
					public void handlePressed() {
						count++;
						updateUI();
					}
				};
			}
			return countBinder;
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
		SWTButtonBinderDemo comp = new SWTButtonBinderDemo(shell, SWT.NONE);
		Presenter presenter = new Presenter();
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
