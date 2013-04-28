package com.beirtipol.binding.core.binders.tree;

import com.beirtipol.binding.core.binders.AbstractBinder;
import com.beirtipol.binding.core.delegates.ITreeDelegate;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractTreeBinder extends AbstractBinder<ITreeDelegate>
		implements ITreeBinder {
	@Override
	public void updateUI() {
		final ITreeDelegate delegate = getDelegate();
		if (delegate != null) {
			try {
				delegate.setRoots(getRoots());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void setupListeners() {
		getDelegate().addSelectionListener(this);
	}
}