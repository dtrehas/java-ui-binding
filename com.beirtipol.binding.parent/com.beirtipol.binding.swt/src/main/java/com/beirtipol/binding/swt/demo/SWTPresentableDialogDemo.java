package com.beirtipol.binding.swt.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.dialog.AbstractPresentableDialogBinder;
import com.beirtipol.binding.core.binders.widget.AbstractButtonBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.swt.SWTAbstractPresentableComposite;
import com.beirtipol.binding.swt.SWTButtonDelegate;
import com.beirtipol.binding.swt.core.dialog.SWTPresentableDialog;
import com.beirtipol.binding.swt.core.dialog.SWTPresentableDialogDelegate;
import com.beirtipol.binding.swt.demo.SWTPresentableDialogDemo.Presenter;

public class SWTPresentableDialogDemo extends
		SWTAbstractPresentableComposite<Presenter> {
	private Button btnOpenDialog;
	private SWTButtonDelegate openDialogDelegate;
	private SWTPresentableDialogDelegate<SWTButtonBinderDemo.Presenter> dialogDelegate;
	private SWTPresentableDialog<SWTButtonBinderDemo.Presenter> dialog;

	public SWTPresentableDialogDemo(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doCreateControls() {
		setLayout(new GridLayout(2, false));

		// Widgets
		{
			btnOpenDialog = new Button(this, SWT.NONE);
			btnOpenDialog.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT,
					true, false, 1, 1));
			dialog = new ButtonDemoDialog(getShell());

		}
		// Delegates
		{
			openDialogDelegate = new SWTButtonDelegate(btnOpenDialog);
			dialogDelegate = new SWTPresentableDialogDelegate<SWTButtonBinderDemo.Presenter>(
					dialog);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getOpenDialogBinder().setDelegate(openDialogDelegate);
		presenter.getDialogBinder().setDelegate(dialogDelegate);
	}

	class ButtonDemoDialog extends
			SWTPresentableDialog<SWTButtonBinderDemo.Presenter> {
		private SWTButtonBinderDemo swtButtonBinderDemo;

		protected ButtonDemoDialog(Shell parentShell) {
			super(parentShell);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			swtButtonBinderDemo = new SWTButtonBinderDemo(parent, SWT.NONE);
			swtButtonBinderDemo.setLayoutData(new GridData(GridData.FILL_BOTH));
			return swtButtonBinderDemo;
		}

		@Override
		public void setPresenter(SWTButtonBinderDemo.Presenter presenter) {
			swtButtonBinderDemo.setPresenter(presenter);
		}
	}

	class Presenter extends AbstractPresenter {
		private IButtonBinder openDialogBinder;
		private AbstractPresentableDialogBinder<SWTButtonBinderDemo.Presenter> dialogBinder;

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

		public AbstractPresentableDialogBinder<SWTButtonBinderDemo.Presenter> getDialogBinder() {
			if (dialogBinder == null) {
				dialogBinder = new AbstractPresentableDialogBinder<SWTButtonBinderDemo.Presenter>() {

					@Override
					public SWTButtonBinderDemo.Presenter getPresenter() {
						return new SWTButtonBinderDemo.Presenter();
					}
				};
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
		SWTPresentableDialogDemo comp = new SWTPresentableDialogDemo(shell,
				SWT.NONE);
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
