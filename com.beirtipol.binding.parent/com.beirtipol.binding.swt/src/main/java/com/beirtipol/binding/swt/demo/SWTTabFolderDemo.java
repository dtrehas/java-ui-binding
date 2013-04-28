package com.beirtipol.binding.swt.demo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.binders.component.AbstractPresentableComponentBinder;
import com.beirtipol.binding.core.binders.component.AbstractTabFolderBinder;
import com.beirtipol.binding.core.binders.component.AbstractTabItemBinder;
import com.beirtipol.binding.core.binders.component.IPresentableComponentBinder;
import com.beirtipol.binding.core.binders.component.ITabFolderBinder;
import com.beirtipol.binding.core.binders.component.ITabItemBinder;
import com.beirtipol.binding.core.binders.widget.AbstractButtonBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.swt.SWTAbstractPresentableComposite;
import com.beirtipol.binding.swt.SWTButtonDelegate;
import com.beirtipol.binding.swt.SWTPresentableComponentDelegate;
import com.beirtipol.binding.swt.SWTTabFolderDelegate;
import com.beirtipol.binding.swt.demo.SWTTabFolderDemo.Presenter;

public class SWTTabFolderDemo extends SWTAbstractPresentableComposite<Presenter> {

	private CTabFolder tabFolder;
	private Button btnNewTab;
	private SWTTabFolderDelegate tabFolderDelegate;
	private SWTButtonDelegate newTabDelegate;

	public SWTTabFolderDemo(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void doCreateControls() {
		{
			setLayout(new GridLayout());

			btnNewTab = new Button(this, SWT.NONE);
			btnNewTab.setText("+");

			tabFolder = new CTabFolder(this, SWT.BORDER | SWT.CLOSE);
			tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		}
		{
			newTabDelegate = new SWTButtonDelegate(btnNewTab);
			tabFolderDelegate = new SWTTabFolderDelegate(tabFolder) {

				@Override
				protected SWTPresentableComponentDelegate<? extends IPresenter> getComponentDelegate(IPresentableComponentBinder<? extends IPresenter> binder) {
					if (binder.getPresenter() instanceof SWTTextBinderDemo.Presenter) {
						return new SWTPresentableComponentDelegate<SWTTextBinderDemo.Presenter>(new SWTTextBinderDemo(tabFolder, SWT.NONE));
					}
					return null;
				}
			};
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getAddTabBinder().setDelegate(newTabDelegate);
		presenter.getTabFolderBinder().setDelegate(tabFolderDelegate);
	}

	class Presenter extends AbstractPresenter {

		public IButtonBinder addTabBinder;
		public ITabFolderBinder tabFolderBinder;
		private final List<ITabItemBinder> currentTabs = new ArrayList<ITabItemBinder>();

		public ITabFolderBinder getTabFolderBinder() {
			if (tabFolderBinder == null) {
				tabFolderBinder = new AbstractTabFolderBinder() {
					@Override
					public List<ITabItemBinder> getTabItemBinders() {
						return currentTabs;
					}
				};
			}
			return tabFolderBinder;
		}

		public IButtonBinder getAddTabBinder() {
			if (addTabBinder == null) {
				addTabBinder = new AbstractButtonBinder("+") {
					@Override
					public void handlePressed() {
						currentTabs.add(new AbstractTabItemBinder() {
							SWTTextBinderDemo.Presenter presenter = new SWTTextBinderDemo.Presenter(new SWTTextBinderDemo.Person());
							private AbstractPresentableComponentBinder<SWTTextBinderDemo.Presenter> compBinder;

							@Override
							public void onClose() {
								currentTabs.remove(this);
								getTabFolderBinder().updateUI();
							}

							@Override
							public String getTitle() {
								return "Tab[" + currentTabs.indexOf(this) + "]";
							}

							@Override
							public IPresentableComponentBinder<SWTTextBinderDemo.Presenter> getComponentBinder() {
								if (compBinder == null) {
									compBinder = new AbstractPresentableComponentBinder<SWTTextBinderDemo.Presenter>() {

										@Override
										public SWTTextBinderDemo.Presenter getPresenter() {
											return presenter;
										}
									};
								}
								return compBinder;
							}
						});
						getTabFolderBinder().updateUI();
					}
				};
			}
			return addTabBinder;
		}

		@Override
		public void free() {
			// TODO Auto-generated method stub
		}
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT");
		shell.setLayout(new FillLayout());
		SWTTabFolderDemo comp = new SWTTabFolderDemo(shell, SWT.NONE);
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
