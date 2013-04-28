package com.beirtipol.binding.swt.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.widget.AbstractCheckboxBinder;
import com.beirtipol.binding.core.binders.widget.AbstractTextBinder;
import com.beirtipol.binding.core.binders.widget.ICheckBoxBinder;
import com.beirtipol.binding.core.binders.widget.ITextBinder;
import com.beirtipol.binding.swt.SWTAbstractPresentableComposite;
import com.beirtipol.binding.swt.SWTCheckBoxDelegate;
import com.beirtipol.binding.swt.SWTTextDelegate;
import com.beirtipol.binding.swt.demo.SWTCheckboxAndTextBinderDemo.Presenter;

public class SWTCheckboxAndTextBinderDemo extends SWTAbstractPresentableComposite<Presenter> {
	private Button btnEnabled;
	private SWTCheckBoxDelegate btnEnabledDelegate;
	private Text text;
	private SWTTextDelegate textDelegate;

	public SWTCheckboxAndTextBinderDemo(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doCreateControls() {
		setLayout(new GridLayout(2, false));

		// Widgets
		{
			btnEnabled = new Button(this, SWT.CHECK);
			btnEnabled.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));

			text = new Text(this, SWT.BORDER);
			text.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));
		}
		// Delegates
		{
			btnEnabledDelegate = new SWTCheckBoxDelegate(btnEnabled);
			textDelegate = new SWTTextDelegate(text);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getEnabledBinder().setDelegate(btnEnabledDelegate);
		presenter.getTextBinder().setDelegate(textDelegate);
	}

	class Presenter extends AbstractPresenter {
		private ICheckBoxBinder countBinder;
		private ITextBinder textBinder;
		private boolean enabled = true;

		public ICheckBoxBinder getEnabledBinder() {
			if (countBinder == null) {
				countBinder = new AbstractCheckboxBinder("Enabled") {

					@Override
					public Boolean getCheckedFromModel() {
						return enabled;
					}

					@Override
					public void setCheckedIntoModel(boolean checked) {
						enabled = checked;
						getTextBinder().updateUI();
					}
				};
			}
			return countBinder;
		}

		public ITextBinder getTextBinder() {
			if (textBinder == null) {
				textBinder = new AbstractTextBinder() {
					private String text = "Some Text";

					@Override
					public void setTextIntoModel(String text) {
						this.text = text;
					}

					@Override
					public String getTextFromModel() {
						return text;
					}

					@Override
					public boolean isEnabled() {
						return enabled;
					}
				};
			}
			return textBinder;
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
		SWTCheckboxAndTextBinderDemo comp = new SWTCheckboxAndTextBinderDemo(shell, SWT.NONE);
		Presenter presenter = comp.new Presenter();
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
