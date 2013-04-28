package com.beirtipol.binding.core.util;

import java.awt.Color;

public interface ItemBinder<T> {
	T convert(String value);

	String convert(T value);

	Color getBackground(T value);

	Color getForeground(T value);
}