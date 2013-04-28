package com.beirtipol.binding.swt.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.widget.AbstractTextBinder;
import com.beirtipol.binding.core.binders.widget.ITextBinder;
import com.beirtipol.binding.swt.SWTAbstractPresentableComposite;
import com.beirtipol.binding.swt.SWTTextDelegate;
import com.beirtipol.binding.swt.demo.SWTTextBinderDemo.Presenter;

public class SWTTextBinderDemo extends
		SWTAbstractPresentableComposite<Presenter> {
	private Text txtFirst;
	private Text txtLast;
	private Text txtFull;
	private SWTTextDelegate firstDelegate;
	private SWTTextDelegate lastDelegate;
	private SWTTextDelegate fullDelegate;

	public SWTTextBinderDemo(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doCreateControls() {
		setLayout(new GridLayout(2, false));

		// Widgets
		{
			Label lblNewLabel = new Label(this, SWT.NONE);
			lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
					false, false, 1, 1));
			lblNewLabel.setText("First:");

			txtFirst = new Text(this, SWT.BORDER);
			txtFirst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));

			Label lblLast = new Label(this, SWT.NONE);
			lblLast.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
					false, 1, 1));
			lblLast.setText("Last:");

			txtLast = new Text(this, SWT.BORDER);
			txtLast.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));

			Label lblNewLabel_1 = new Label(this, SWT.NONE);
			lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
					false, false, 1, 1));
			lblNewLabel_1.setText("Full:");

			txtFull = new Text(this, SWT.BORDER);
			txtFull.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
		}
		// Delegates
		{
			firstDelegate = new SWTTextDelegate(txtFirst);
			lastDelegate = new SWTTextDelegate(txtLast);
			fullDelegate = new SWTTextDelegate(txtFull);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getFirstBinder().setDelegate(firstDelegate);
		presenter.getLastBinder().setDelegate(lastDelegate);
		presenter.getFullBinder().setDelegate(fullDelegate);
	}

	static class Presenter extends AbstractPresenter {

		private ITextBinder firstBinder;
		private ITextBinder lastBinder;
		private ITextBinder fullBinder;

		private final Person bom;

		public Presenter(Person bom) {
			this.bom = bom;
		}

		public ITextBinder getFirstBinder() {
			if (firstBinder == null) {
				firstBinder = new AbstractTextBinder() {
					@Override
					public void setTextIntoModel(String text) {
						bom.firstName = text;
						getFullBinder().updateUI();
					}

					@Override
					public String getTextFromModel() {
						return bom.firstName;
					}
				};
			}
			return firstBinder;
		}

		public ITextBinder getLastBinder() {
			if (lastBinder == null) {
				lastBinder = new AbstractTextBinder() {
					@Override
					public void setTextIntoModel(String text) {
						bom.lastName = text;
						getFullBinder().updateUI();
					}

					@Override
					public String getTextFromModel() {
						return bom.lastName;
					}
				};
			}
			return lastBinder;
		}

		public ITextBinder getFullBinder() {
			if (fullBinder == null) {
				fullBinder = new AbstractTextBinder() {
					@Override
					public void setTextIntoModel(String text) {
						// Not allowed
					}

					@Override
					public boolean isEnabled() {
						return false;
					}

					@Override
					public String getTextFromModel() {
						return bom.firstName + " " + bom.lastName;
					}
				};
			}
			return fullBinder;
		}

		@Override
		public void free() {
		}
	}

	static class Person {
		String firstName = "First";
		String lastName = "Last";
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT");
		shell.setLayout(new FillLayout());
		SWTTextBinderDemo comp = new SWTTextBinderDemo(shell, SWT.NONE);
		Presenter presenter = new Presenter(new Person());
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
