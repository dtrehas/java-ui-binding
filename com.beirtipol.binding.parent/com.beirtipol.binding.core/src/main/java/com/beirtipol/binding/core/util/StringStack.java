package com.beirtipol.binding.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Immutable String Stack. TODO: Rename to ObjectStack<T> and have an
 * implementation for String.
 * 
 * @author O041484
 */
public class StringStack {
	private final String[] path;

	public StringStack(String... segments) {
		this.path = segments;
	}

	public StringStack(Collection<String> segments) {
		this.path = segments.toArray(new String[0]);
	}

	public boolean startsWith(StringStack other) {
		String[] trimmed = (String[]) ArrayUtils.subarray(path, 0,
				other.path.length);
		return Arrays.equals(other.path, trimmed);
	}

	/**
	 * @return a new StringStack minus the first element
	 */
	public StringStack drop() {
		return drop(1);
	}

	/**
	 * @return a new StringStack minus the specified number of elements
	 */
	public StringStack drop(int num) {
		return new StringStack((String[]) ArrayUtils.subarray(path, num,
				path.length));
	}

	/**
	 * @return a new StringStack minus the specified path, or an empty stack if
	 *         the path is invalid.
	 */
	public StringStack drop(StringStack pathToDropTo) {
		if (startsWith(pathToDropTo)) {
			return drop(pathToDropTo.size());
		} else {
			return new StringStack();
		}
	}

	/**
	 * @param segment
	 *            the new segment to push on to the top of the stack
	 * @return a new StringStack containing the prefix as the first element in
	 *         the stack.
	 */
	public StringStack push(String segment) {
		String[] newPath = new String[path.length + 1];
		newPath[0] = segment;
		System.arraycopy(path, 0, newPath, 1, path.length);
		return new StringStack(newPath);
	}

	public StringStack push(StringStack prefix) {
		String[] newPath = new String[path.length + prefix.size()];
		System.arraycopy(prefix.path, 0, newPath, 0, prefix.path.length);
		System.arraycopy(path, 0, newPath, prefix.path.length, path.length);
		return new StringStack(newPath);
	}

	/**
	 * @return the first element in the stack or null if the stack is empty
	 */
	public String peek() {
		if (path.length > 0) {
			return path[0];
		}
		return null;
	}

	@Override
	public String toString() {
		return StringUtils.join(path, ".");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof StringStack) {
			return Arrays.equals(path, ((StringStack) obj).path);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public static List<StringStack> asColumnPathList(List<String> asStrings) {
		List<StringStack> result = new ArrayList<StringStack>();
		for (String str : asStrings) {
			result.add(new StringStack(str));
		}
		return result;
	}

	public static List<String> asStringList(List<StringStack> asPaths) {
		List<String> result = new ArrayList<String>();
		for (StringStack path : asPaths) {
			result.add(path.toString());
		}
		return result;
	}

	public static List<StringStack> pushAll(String segment,
			List<StringStack> paths) {
		List<StringStack> result = new ArrayList<StringStack>();
		for (StringStack columnPath : paths) {
			result.add(columnPath.push(segment));
		}
		return result;
	}

	public int size() {
		return path.length;
	}

	public String[] toArray() {
		return path;
	}

}