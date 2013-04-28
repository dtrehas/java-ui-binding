package com.beirtipol.binding.core.tree.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beirtipol.binding.core.tree.ITreeNode;
import com.beirtipol.binding.core.tree.ITypedTreeNode;
import com.beirtipol.binding.core.tree.TreeNodeAdapter;
import com.beirtipol.binding.core.util.ReflectionUtils;

public class BasicReflectionTreeNode extends TreeNodeAdapter implements
		ITypedTreeNode, Comparable<BasicReflectionTreeNode> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BasicReflectionTreeNode.class);

	protected final Object model;
	private final String name;
	protected final BasicReflectionTreeNodeIDContext idContext;

	public BasicReflectionTreeNode(ITreeNode parent, String name, Object model,
			BasicReflectionTreeNodeIDContext idContext) {
		super(parent);
		this.model = model;
		this.name = name;
		this.idContext = idContext;
	}

	@Override
	public void createChildren(List<ITreeNode> children) {
		if (model == null) {
			return;
		}
		List<Field> fields = ReflectionUtils.getAllDeclaredFields(model
				.getClass());
		for (Field f : fields) {
			if ("this$0".equals(f.getName())
					|| f.getName().startsWith("$SWITCH_TABLE")) {
				continue;
			}
			if (((f.getModifiers() & Modifier.STATIC) == Modifier.STATIC)) {
				continue;
			}
			try {
				f.setAccessible(true);
				Object value = f.get(model);
				if (ObjectUtils.equals(getValue(), value)) {
					// Certain classes are self-referential
					continue;
				}
				ITypedTreeNode childNode = TreeNodeFactory.createTreeNode(this,
						f.getName(), value, idContext);
				children.add(childNode);
			} catch (IllegalAccessException e) {
				LOGGER.error(
						String.format(
								"IllegalAccessException when trying to get the value from field '%s' of object '%s'",
								f.getName(), model), e);
				continue;
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		if (model == null) {
			return "[null]";
		}
		return model.toString();
	}

	@Override
	public String getType() {
		if (model == null) {
			return "[null]";
		}
		return getObjectTypeString() + " (id=" + idContext.getId(model) + ")";
	}

	protected String getObjectTypeString() {
		return model.getClass().getSimpleName();
	}

	@Override
	public boolean equals(Object obj) {
		return ObjectUtils.equals(obj, model);
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(model);
	}

	@Override
	public Object getValue() {
		return model;
	}

	@Override
	public int compareTo(BasicReflectionTreeNode o) {
		return getName().compareTo(o.getName());
	}

}