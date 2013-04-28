package com.beirtipol.binding.core.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnumUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnumUtils.class);

	public static String valuesAsReadableString(Class<? extends Enum<?>> enumType) {
		String[] enumValues = valuesAsStrings(enumType);
		return Arrays.toString(enumValues);
	}

	public static String[] valuesAsStrings(Class<? extends Enum<?>> enumType) {
		String[] enumValues = new String[enumType.getEnumConstants().length];
		for (int i = 0; i < enumValues.length; i++) {
			enumValues[i] = enumType.getEnumConstants()[i].toString();
		}
		return enumValues;
	}

	public static String[] valuesAsStrings(Enum<?>... enums) {
		String[] enumValues = new String[enums.length];
		for (int i = 0; i < enumValues.length; i++) {
			enumValues[i] = enums[i].toString();
		}
		return enumValues;
	}

	public static String[] enumSetAsStrings(EnumSet<?> enumType) {

		String[] enumValues = new String[enumType.size()];

		Iterator<?> i = enumType.iterator();
		int index = 0;
		while (i.hasNext()) {

			enumValues[index] = i.next().toString();
			index++;
		}

		return enumValues;

	}

	public static List<String> valuesAsStringList(Class<? extends Enum<?>> enumType) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < enumType.getEnumConstants().length; i++) {
			result.add(enumType.getEnumConstants()[i].toString());
		}
		return result;
	}

	/**
	 * Gets the enum toString with null safety
	 * 
	 * @param <T>
	 * @return - the string that corresponds to the enum element
	 */
	public static <T extends Enum<?>> String nullSafeToString(T enumInstance) {
		String string;

		if (enumInstance == null) {
			string = "";
		} else {
			string = enumInstance.toString();
		}

		return string;
	}

	/**
	 * A simple parse method that compares the input string with the enum
	 * value's toString(). It will look for a static 'parse(String)' method on
	 * the enum first. The check is case sensitive, it's suggested that any
	 * fuzzy parsing logic is done in the presentation model rather then the
	 * enum. Also consider the use of ListHelper.parse() for the presentation
	 * model as this handles some fuzzy matching (mixed case, start of word
	 * etc).
	 * 
	 * @param <T>
	 * @param enumClass
	 * @param stringToParse
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<?>> T parse(Class<T> enumClass, String stringToParse) {
		try {
			Method parseMethod = enumClass.getDeclaredMethod("parse", String.class);
			if (parseMethod != null) {
				parseMethod.setAccessible(true);

				return (T) parseMethod.invoke(null, stringToParse);
			}
		} catch (Exception e) {
			LOGGER.error("Error invoking parse method on " + enumClass, e);
		}
		for (T value : enumClass.getEnumConstants()) {
			if (value.toString().equals(stringToParse)) {
				return value;
			}
		}

		return null;
	}

	public static <T extends Enum<?>> T parse(Class<T> enumClass, Object objToParse) {
		if (objToParse != null) {
			return parse(enumClass, objToParse.toString());
		}

		return null;
	}

	public static <T extends Enum<?>> T findFirstMatching(Class<T> enumClass, String stringToParse) {
		for (T value : enumClass.getEnumConstants()) {
			if (value.toString().toUpperCase().startsWith(stringToParse.toUpperCase())) {
				return value;
			}
		}

		return null;
	}
}
