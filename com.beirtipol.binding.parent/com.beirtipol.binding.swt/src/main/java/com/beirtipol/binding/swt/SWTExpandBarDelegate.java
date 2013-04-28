package com.beirtipol.binding.swt;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.binders.component.IExpandItemBinder;
import com.beirtipol.binding.core.binders.component.IPresentableComponentBinder;
import com.beirtipol.binding.core.delegates.IExpandBarDelegate;

public abstract class SWTExpandBarDelegate implements IExpandBarDelegate {
	private final ExpandBar control;
	private final Map<IExpandItemBinder, ExpandItem> myBinders = new IdentityHashMap<IExpandItemBinder, ExpandItem>();

	public SWTExpandBarDelegate(ExpandBar control) {
		this.control = control;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setExpandItemBinders(final Collection<IExpandItemBinder> currentBinders) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (control == null || control.isDisposed()) {
					return;
				}
				// Remove any binders not required
				Collection<IExpandItemBinder> bindersToRemove = CollectionUtils.subtract(myBinders.keySet(), currentBinders);
				for (IExpandItemBinder eiBinder : SWTExpandBarDelegate.this.myBinders.keySet()) {
					if (bindersToRemove.contains(eiBinder)) {
						eiBinder.getDelegate().free();
						eiBinder.setDelegate(null);
					}
				}
				for (IExpandItemBinder eiBinder : bindersToRemove) {
					eiBinder.free();
					myBinders.remove(eiBinder);
				}

				// Add any new binders
				Collection<IExpandItemBinder> newBinders = CollectionUtils.subtract(currentBinders, myBinders.keySet());
				for (IExpandItemBinder eiBinder : newBinders) {
					final ExpandItem taskPane = new ExpandItem(control, SWT.NONE);
					SWTExpandItemDelegate delegate = new SWTExpandItemDelegate(taskPane);
					eiBinder.setDelegate(delegate);

					IPresentableComponentBinder<?> itemBinder = eiBinder.getComponentBinder();
					SWTPresentableComponentDelegate<? extends IPresenter> componentDelegate = getComponentDelegate(itemBinder);
					final SWTAbstractPresentableComposite eiControl = componentDelegate.getComponent();
					taskPane.setControl(eiControl);
					eiBinder.getComponentBinder().setDelegate(new SWTPresentableComponentDelegate(eiControl));
					eiBinder.updateUI();

					// ExpandItem needs to be resized after the screen has been
					// drawn in the
					// presentable panel.
					Point size = eiControl.computeSize(SWT.DEFAULT, SWT.DEFAULT);
					if (taskPane.getHeight() != size.y) {
						taskPane.setHeight(size.y);
					}
					myBinders.put(eiBinder, taskPane);
				}

				// Update any remaining binders
				for (IExpandItemBinder binder : newBinders) {
					binder.updateUI();
				}

				// This is necessary to get the child ExpandItems to render
				// properly. It's important
				// to call it on the ExpandBar and not the parent as that makes
				// everything resize
				// all stupid-like.
				control.pack();
			}
		});
	}

	/**
	 * @param binder
	 * @return a {@link SWTPresentableComponentDelegate} which has been
	 *         initialised with the correct panel for the supplied binder.
	 */
	protected abstract SWTPresentableComponentDelegate<? extends IPresenter> getComponentDelegate(IPresentableComponentBinder<? extends IPresenter> binder);

	@Override
	public void free() {
	}
}