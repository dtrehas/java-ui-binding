package com.beirtipol.binding.swing.util;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import org.apache.log4j.Logger;

/**
 * Utility for multi screen interaction i.e. when Toolkit should not be used.
 * 
 * @author David Ainslie
 */
public final class GraphicsDeviceUtil {
	private static final Logger LOGGER = Logger.getLogger(GraphicsDeviceUtil.class);

	/**
	 * Make sure that a component can be completely seen on the relevant screen,
	 * i.e. makes any necessary adjustments to the component's location. The
	 * relevant screen is chosen by which one holds the top left corner point of
	 * the given component. Note - if this point is not on any screen, then the
	 * default screen is chosen.
	 * 
	 * @param component
	 */
	public static void ensureOnScreen(Component component) {
		Rectangle componentBounds = component.getBounds();
		Rectangle graphicsDeviceBounds = getGraphicsDeviceBounds(component);

		if (graphicsDeviceBounds == null) {
			LOGGER.warn(String.format("Odd! The given component %s is not on any screen! Will use the default screen", component));
			graphicsDeviceBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		}

		int componentXAdjustment = 0;
		int componentYAdjustment = 0;

		if ((componentBounds.x + componentBounds.width) > (graphicsDeviceBounds.x + graphicsDeviceBounds.width)) {
			componentXAdjustment = (componentBounds.x + componentBounds.width) - (graphicsDeviceBounds.x + graphicsDeviceBounds.width);
		}

		if ((componentBounds.y + componentBounds.height) > (graphicsDeviceBounds.y + graphicsDeviceBounds.height)) {
			componentYAdjustment = (componentBounds.y + componentBounds.height) - (graphicsDeviceBounds.y + graphicsDeviceBounds.height);
		}

		component.setBounds(componentBounds.x - componentXAdjustment, componentBounds.y - componentYAdjustment, componentBounds.width, componentBounds.height);
	}

	/**
	 * Get the bounds (x, y, width, height) of the screen where the given
	 * component lies or "null" if not on any screen.
	 * 
	 * @param component
	 * @return
	 */
	public static Rectangle getGraphicsDeviceBounds(Component component) {
		Rectangle componentBounds = component.getBounds();

		for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			for (GraphicsConfiguration graphicsConfiguration : graphicsDevice.getConfigurations()) {
				Rectangle graphicsDeviceBounds = graphicsConfiguration.getBounds();

				if (componentBounds.x >= graphicsDeviceBounds.x && componentBounds.x <= graphicsDeviceBounds.x + graphicsDeviceBounds.width && componentBounds.y >= graphicsDeviceBounds.y && componentBounds.y <= graphicsDeviceBounds.y + graphicsDeviceBounds.height) {
					return graphicsDeviceBounds;
				}
			}
		}

		LOGGER.warn(String.format("The given component %s is not on any screen!", component));
		return null;
	}

	/**
	 *
	 */
	private GraphicsDeviceUtil() {
	}
}