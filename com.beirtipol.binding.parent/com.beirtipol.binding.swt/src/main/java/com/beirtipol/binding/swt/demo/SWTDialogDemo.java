package com.beirtipol.binding.swt.demo;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.dialog.DialogBinder;
import com.beirtipol.binding.core.binders.widget.AbstractButtonBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.core.delegates.IDialogDelegate;
import com.beirtipol.binding.swt.SWTAbstractPresentableComposite;
import com.beirtipol.binding.swt.SWTButtonDelegate;
import com.beirtipol.binding.swt.core.dialog.SWTDialogDelegate;
import com.beirtipol.binding.swt.demo.SWTDialogDemo.Presenter;

public class SWTDialogDemo extends SWTAbstractPresentableComposite<Presenter> {
	private Button btnOpenDialog;
	private SWTButtonDelegate chooseFileDelegate;
	private SWTDialogDelegate dialogDelegate;
	private Dialog dialog;

	public SWTDialogDemo(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doCreateControls() {
		setLayout(new GridLayout(2, false));

		// Widgets
		{
			btnOpenDialog = new Button(this, SWT.NONE);
			btnOpenDialog.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT,
					false, false, 1, 1));
			dialog = new Dialog(getShell()) {
			};

		}
		// Delegates
		{
			chooseFileDelegate = new SWTButtonDelegate(btnOpenDialog);
			dialogDelegate = new SWTDialogDelegate(dialog);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getOpenDialogBinder().setDelegate(chooseFileDelegate);
		presenter.getDialogBinder().setDelegate(dialogDelegate);
	}

	class Presenter extends AbstractPresenter {
		private IButtonBinder openDialogBinder;
		private DialogBinder<IDialogDelegate> dialogBinder;

		public IButtonBinder getOpenDialogBinder() {
			if (openDialogBinder == null) {
				openDialogBinder = new AbstractButtonBinder() {
					@Override
					public String getText() {
						return "Open";
					}

					@Override
					public void handlePressed() {
						getDialogBinder().open();
					}
				};
			}
			return openDialogBinder;
		}

		public DialogBinder<IDialogDelegate> getDialogBinder() {
			if (dialogBinder == null) {
				dialogBinder = new DialogBinder<IDialogDelegate>();
			}
			return dialogBinder;
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
		SWTDialogDemo comp = new SWTDialogDemo(shell, SWT.NONE);
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
