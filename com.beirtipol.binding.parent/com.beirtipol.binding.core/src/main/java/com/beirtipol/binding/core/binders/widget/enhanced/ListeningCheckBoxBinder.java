package com.beirtipol.binding.core.binders.widget.enhanced;

import com.beirtipol.binding.core.binders.AbstractListeningBinder;
import com.beirtipol.binding.core.binders.widget.ICheckBoxBinder;
import com.beirtipol.binding.core.delegates.ICheckBoxDelegate;
import com.beirtipol.binding.core.pcs.StaticObjectFormattedFieldAccessor;

public class ListeningCheckBoxBinder extends AbstractListeningBinder<ICheckBoxDelegate> implements ICheckBoxBinder {

	private final String initialText;

	public ListeningCheckBoxBinder(final StaticObjectFormattedFieldAccessor<Boolean> accessor, final String initialText) {
		super(accessor);
		this.initialText = initialText;
	}

	@Override
	public void updateUI() {
		final ICheckBoxDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.setVisible(isVisible());
			if (!isVisible()) {
				return;
			}
			Boolean checkedFromModel = getCheckedFromModel();
			checkedFromModel = checkedFromModel != null ? checkedFromModel : false;
			delegate.setChecked(checkedFromModel);
			delegate.setEnabled(isEnabled());
			delegate.setText(getText());
		}
	}

	@Override
	public String getText() {
		return initialText;
	}

	@Override
	protected void setupListeners() {
		ICheckBoxDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.addPressedListener(this);
		}
	}

	@Override
	public Boolean getCheckedFromModel() {
		return Boolean.parseBoolean(accessor.getValue());
	}

	@Override
	public void setCheckedIntoModel(boolean checked) {
		accessor.setValue("" + checked);
	}
}
