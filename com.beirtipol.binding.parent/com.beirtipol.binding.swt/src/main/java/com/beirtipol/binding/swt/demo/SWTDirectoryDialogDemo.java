package com.beirtipol.binding.swt.demo;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.dialog.DirectoryDialogBinder;
import com.beirtipol.binding.core.binders.widget.AbstractButtonBinder;
import com.beirtipol.binding.core.binders.widget.AbstractTextBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.core.binders.widget.ITextBinder;
import com.beirtipol.binding.swt.SWTAbstractPresentableComposite;
import com.beirtipol.binding.swt.SWTButtonDelegate;
import com.beirtipol.binding.swt.SWTTextDelegate;
import com.beirtipol.binding.swt.core.dialog.SWTDirectoryDialogDelegate;
import com.beirtipol.binding.swt.demo.SWTDirectoryDialogDemo.Presenter;

public class SWTDirectoryDialogDemo extends SWTAbstractPresentableComposite<Presenter> {
	private Button btnChooseFile;
	private SWTButtonDelegate chooseFileDelegate;
	private Text txtChosenFile;
	private SWTTextDelegate chosenTextDelegate;
	private SWTDirectoryDialogDelegate fileChooserDelegate;

	public SWTDirectoryDialogDemo(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doCreateControls() {
		setLayout(new GridLayout(2, false));

		// Widgets
		{
			txtChosenFile = new Text(this, SWT.BORDER);
			txtChosenFile.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 1, 1));

			btnChooseFile = new Button(this, SWT.NONE);
			btnChooseFile.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, false, false, 1, 1));

		}
		// Delegates
		{
			chooseFileDelegate = new SWTButtonDelegate(btnChooseFile);
			chosenTextDelegate = new SWTTextDelegate(txtChosenFile);
			fileChooserDelegate = new SWTDirectoryDialogDelegate(new DirectoryDialog(getShell()));
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getChooseFileBinder().setDelegate(chooseFileDelegate);
		presenter.getChosenFileBinder().setDelegate(chosenTextDelegate);
		presenter.getFileChooserBinder().setDelegate(fileChooserDelegate);
	}

	class Presenter extends AbstractPresenter {
		private IButtonBinder chooseFileBinder;
		private DirectoryDialogBinder fileChooserBinder;
		private ITextBinder chosenFileBinder;

		private File chosenFile;

		public IButtonBinder getChooseFileBinder() {
			if (chooseFileBinder == null) {
				chooseFileBinder = new AbstractButtonBinder() {
					@Override
					public String getText() {
						return "...";
					}

					@Override
					public void handlePressed() {
						getFileChooserBinder().open();
					}
				};
			}
			return chooseFileBinder;
		}

		public DirectoryDialogBinder getFileChooserBinder() {
			if (fileChooserBinder == null) {
				fileChooserBinder = new DirectoryDialogBinder() {

					@Override
					protected String getFilterPath() {
						return "c:\\";
					}

					@Override
					public void onClose(File file) {
						chosenFile = file;
						getChosenFileBinder().updateUI();
					}
				};
			}
			return fileChooserBinder;
		}

		public ITextBinder getChosenFileBinder() {
			if (chosenFileBinder == null) {
				chosenFileBinder = new AbstractTextBinder() {

					@Override
					public boolean isEnabled() {
						return false;
					}

					@Override
					public void setTextIntoModel(String text) {
					}

					@Override
					public String getTextFromModel() {
						return chosenFile == null ? "" : chosenFile.getAbsolutePath();
					}
				};
			}
			return chosenFileBinder;
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
		SWTDirectoryDialogDemo comp = new SWTDirectoryDialogDemo(shell, SWT.NONE);
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
