package com.beirtipol.binding.core.tree;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.List;

import com.beirtipol.binding.core.tree.reflection.BasicReflectionTreeNode;
import com.beirtipol.binding.core.tree.reflection.BasicReflectionTreeNodeIDContext;

public class TreeNodePrinter {
	/**
	 * @param stream
	 *            The stream to write the output to
	 * @param maxDepth
	 *            If this is <=0, the printing will continue until all nodes
	 *            have been reached.
	 * @param nodes
	 *            The nodes to write on the stream
	 */
	public static void print(PrintStream stream, int maxDepth,
			ITreeNode... nodes) {
		stream.print(toString(maxDepth, nodes));
	}

	/**
	 * @param maxDepth
	 *            If this is <=0, the printing will continue until all nodes
	 *            have been reached.
	 * @param nodes
	 *            The nodes to write as a String and return
	 * @return
	 */
	public static String toString(int maxDepth, ITreeNode... nodes) {
		StringWriter sw = new StringWriter();
		for (ITreeNode node : nodes) {
			serializeNode(sw, "", node, maxDepth, 0);
		}
		return sw.toString();
	}

	/**
	 * @param writer
	 *            The writer to append the node output to
	 * @param prefix
	 *            The prefix for each node
	 * @param node
	 *            The node to write
	 */
	private static void serializeNode(StringWriter writer, String prefix,
			ITreeNode node, int maxDepth, int currentDepth) {
		writer.append(prefix);
		writer.append(node.getName() + ": " + node.getClass().getSimpleName()
				+ ": " + node.toString());
		writer.append("\n");
		if (node.hasChildren()) {
			if (currentDepth == maxDepth) {
				writer.append("**** Max Depth (" + currentDepth
						+ ") Exceeded ****\n");
				return;
			}
			currentDepth++;
			prefix = prefix.substring(0, Math.max(prefix.length() - 2, 0))
					+ "| ";
			List<ITreeNode> children = node.getChildren();
			for (int i = 0; i < children.size(); i++) {
				ITreeNode child = children.get(i);
				String childPrefix;
				if (child.hasChildren()) {
					childPrefix = "+-";
				} else {
					if (i == children.size() - 1) {
						childPrefix = "\\-";
					} else {
						childPrefix = "|-";
					}
				}
				serializeNode(writer, prefix + childPrefix, child, maxDepth,
						currentDepth);
			}
			currentDepth--;
		}
	}

	public static void main(String[] args) {
		print(System.out, 1, new BasicReflectionTreeNode(null, "Root",
				"ROotasdasd", new BasicReflectionTreeNodeIDContext()));
	}
}
