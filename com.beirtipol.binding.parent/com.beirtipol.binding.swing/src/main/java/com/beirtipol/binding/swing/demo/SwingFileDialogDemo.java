package com.beirtipol.binding.swing.demo;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.dialog.FileDialogBinder;
import com.beirtipol.binding.core.binders.widget.AbstractButtonBinder;
import com.beirtipol.binding.core.binders.widget.AbstractTextBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.core.binders.widget.ITextBinder;
import com.beirtipol.binding.swing.AbstractPresentableSwingPanel;
import com.beirtipol.binding.swing.SwingButtonDelegate;
import com.beirtipol.binding.swing.SwingTextDelegate;
import com.beirtipol.binding.swing.core.dialog.SwingFileDialogDelegate;
import com.beirtipol.binding.swing.demo.SwingFileDialogDemo.Presenter;

public class SwingFileDialogDemo extends AbstractPresentableSwingPanel<Presenter> {
	private JButton btnChooseFile;
	private SwingButtonDelegate chooseFileDelegate;
	private JTextField txtChosenFile;
	private SwingTextDelegate chosenTextDelegate;
	private SwingFileDialogDelegate fileChooserDelegate;

	@Override
	protected JComponent createSwingComponent() {
		JPanel result = new JPanel();
		result.setLayout(new MigLayout("insets 0 0 0 0"));

		// Widgets
		{
			txtChosenFile = new JTextField();
			result.add(txtChosenFile, "growx, pushx");

			btnChooseFile = new JButton();
			result.add(btnChooseFile);

		}
		// Delegates
		{
			chooseFileDelegate = new SwingButtonDelegate(btnChooseFile);
			chosenTextDelegate = new SwingTextDelegate(txtChosenFile);
			fileChooserDelegate = new SwingFileDialogDelegate(new JFileChooser(), result);
		}
		return result;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getChooseFileBinder().setDelegate(chooseFileDelegate);
		presenter.getChosenFileBinder().setDelegate(chosenTextDelegate);
		presenter.getFileChooserBinder().setDelegate(fileChooserDelegate);
	}

	class Presenter extends AbstractPresenter {
		private IButtonBinder chooseFileBinder;
		private FileDialogBinder fileChooserBinder;
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

		public FileDialogBinder getFileChooserBinder() {
			if (fileChooserBinder == null) {
				fileChooserBinder = new FileDialogBinder() {

					@Override
					protected Map<String, String> getFilters() {
						Map<String, String> result = new LinkedHashMap<String, String>();
						result.put("Text Files (*.txt)", "*.txt");
						result.put("All File Types (*.*)", "*.*");
						return result;

					}

					@Override
					protected String getFileName() {
						return "boot.ini";
					}

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
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		final JFrame frame = new JFrame("Swing");

		SwingFileDialogDemo panel = new SwingFileDialogDemo();
		Presenter presenter = panel.new Presenter();
		frame.getContentPane().add(panel.getSwingComponent());
		panel.setPresenter(presenter);
		presenter.updateUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
