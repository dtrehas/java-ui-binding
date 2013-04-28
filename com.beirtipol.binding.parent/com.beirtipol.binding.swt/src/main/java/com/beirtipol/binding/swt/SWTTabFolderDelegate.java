package com.beirtipol.binding.swt;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.binders.component.IPresentableComponentBinder;
import com.beirtipol.binding.core.binders.component.ITabItemBinder;
import com.beirtipol.binding.core.delegates.ITabFolderDelegate;

public abstract class SWTTabFolderDelegate implements ITabFolderDelegate {

	private CTabFolder tabFolder;
	private final Map<ITabItemBinder, CTabItem> myBinders = new IdentityHashMap<ITabItemBinder, CTabItem>();

	public SWTTabFolderDelegate(CTabFolder tabFolder) {
		this.tabFolder = tabFolder;
	}

	@Override
	public void free() {
		tabFolder = null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setTabItemBinders(final Collection<ITabItemBinder> currentBinders) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (tabFolder == null || tabFolder.isDisposed()) {
					return;
				}
				// Remove any binders not required
				Collection<ITabItemBinder> bindersToRemove = CollectionUtils.subtract(myBinders.keySet(), currentBinders);
				for (ITabItemBinder eiBinder : SWTTabFolderDelegate.this.myBinders.keySet()) {
					if (bindersToRemove.contains(eiBinder)) {
						eiBinder.getDelegate().free();
						eiBinder.setDelegate(null);
					}
				}
				for (ITabItemBinder eiBinder : bindersToRemove) {
					eiBinder.free();
					myBinders.remove(eiBinder);
				}

				// Add any new binders
				Collection<ITabItemBinder> newBinders = CollectionUtils.subtract(currentBinders, myBinders.keySet());
				for (ITabItemBinder eiBinder : newBinders) {
					final CTabItem taskPane = new CTabItem(tabFolder, SWT.NONE);
					SWTTabItemDelegate delegate = new SWTTabItemDelegate(taskPane);
					eiBinder.setDelegate(delegate);

					IPresentableComponentBinder<?> itemBinder = eiBinder.getComponentBinder();
					SWTPresentableComponentDelegate<? extends IPresenter> componentDelegate = getComponentDelegate(itemBinder);
					final SWTAbstractPresentableComposite eiControl = componentDelegate.getComponent();
					taskPane.setControl(eiControl);
					eiBinder.getComponentBinder().setDelegate(new SWTPresentableComponentDelegate(eiControl));
					eiBinder.updateUI();
					myBinders.put(eiBinder, taskPane);
				}

				// Update any remaining binders
				for (ITabItemBinder binder : newBinders) {
					binder.updateUI();
				}
				if (myBinders.size() > 0) {
					tabFolder.setSelection(0);
				}

				// This is necessary to get the child ExpandItems to render
				// properly. It's important
				// to call it on the ExpandBar and not the parent as that makes
				// everything resize
				// all stupid-like.
				// control.pack();
			}
		});
	}

	/**
	 * @param binder
	 * @return a {@link SWTPresentableComponentDelegate} which has been
	 *         initialised with the correct panel for the supplied binder.
	 */
	protected abstract SWTPresentableComponentDelegate<? extends IPresenter> getComponentDelegate(IPresentableComponentBinder<? extends IPresenter> binder);

}
