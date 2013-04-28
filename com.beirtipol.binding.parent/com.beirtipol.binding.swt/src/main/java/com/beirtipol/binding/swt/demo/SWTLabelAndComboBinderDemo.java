package com.beirtipol.binding.swt.demo;

import java.awt.Color;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.widget.AbstractComboBinder;
import com.beirtipol.binding.core.binders.widget.AbstractLabelBinder;
import com.beirtipol.binding.core.binders.widget.IComboBinder;
import com.beirtipol.binding.core.binders.widget.ILabelBinder;
import com.beirtipol.binding.core.util.ItemBinder;
import com.beirtipol.binding.swt.SWTAbstractPresentableComposite;
import com.beirtipol.binding.swt.SWTComboDelegate;
import com.beirtipol.binding.swt.SWTLabelDelegate;
import com.beirtipol.binding.swt.demo.SWTLabelAndComboBinderDemo.Presenter;

public class SWTLabelAndComboBinderDemo extends
		SWTAbstractPresentableComposite<Presenter> {
	private Combo cmbColor;
	private SWTComboDelegate colorDelegate;

	public SWTLabelAndComboBinderDemo(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doCreateControls() {
		setLayout(new GridLayout(2, false));

		// Widgets
		{
			colorLabel = new Label(this, SWT.NONE);
			colorLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
					false, 1, 1));
			colorLabel.setText("Color:");

			cmbColor = new Combo(this, SWT.READ_ONLY);
			cmbColor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
		}
		// Delegates
		{
			labelDelegate = new SWTLabelDelegate(colorLabel);
			colorDelegate = new SWTComboDelegate(cmbColor);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getColorBinder().setDelegate(colorDelegate);
		presenter.getLblBinder().setDelegate(labelDelegate);
	}

	private static BidiMap colorMap = new DualHashBidiMap();
	private Label colorLabel;
	private SWTLabelDelegate labelDelegate;
	static {
		colorMap.put("Black", Color.BLACK);
		colorMap.put("Blue", Color.BLUE);
		colorMap.put("Yellow", Color.YELLOW);
		colorMap.put("Red", Color.RED);
	}

	class Presenter extends AbstractPresenter {
		private ILabelBinder lblBinder;
		private IComboBinder colorBinder;
		private Color selectedColor;

		public ILabelBinder getLblBinder() {
			if (lblBinder == null) {
				lblBinder = new AbstractLabelBinder() {

					@Override
					public Color getForeground() {
						return selectedColor;
					}

					@Override
					public String getTextFromModel() {
						return "Color";
					}
				};
			}
			return lblBinder;
		}

		public IComboBinder getColorBinder() {
			if (colorBinder == null) {
				colorBinder = new AbstractComboBinder() {

					@Override
					public void setSelectedItem(Object o) {
						selectedColor = (Color) o;
						getLblBinder().updateUI();
					}

					@Override
					public Object getSelectedItem() {
						return selectedColor;
					}

					@SuppressWarnings("rawtypes")
					@Override
					public ItemBinder getItemBinder() {
						return new ItemBinder<Color>() {
							@Override
							public String convert(Color value) {
								return (String) colorMap.getKey(value);
							}

							@Override
							public Color getBackground(Color value) {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public Color getForeground(Color value) {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public Color convert(String value) {
								// TODO Auto-generated method stub
								return null;
							}
						};
					}

					@Override
					public Object[] getAvailableItems() {
						return colorMap.values().toArray();
					}
				};
			}
			return colorBinder;
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
		SWTLabelAndComboBinderDemo comp = new SWTLabelAndComboBinderDemo(shell,
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
