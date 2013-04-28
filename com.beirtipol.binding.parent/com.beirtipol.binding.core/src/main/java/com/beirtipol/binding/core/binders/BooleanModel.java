package com.beirtipol.binding.core.binders;

public class BooleanModel {
	public static IBooleanModel FALSE = new IBooleanModel() {
		@Override
		public boolean isTrue() {
			return false;
		}
	};
	public static IBooleanModel TRUE = new IBooleanModel() {
		@Override
		public boolean isTrue() {
			return true;
		}
	};
}
