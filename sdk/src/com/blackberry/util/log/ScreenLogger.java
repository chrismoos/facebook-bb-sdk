package com.blackberry.util.log;

import java.util.Date;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ScreenLogger extends AbstractLogger {

	protected MainScreen screen;
	protected VerticalFieldManager vfm;

	protected ScreenLogger(String name) {
		screen = new MainScreen(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR | Manager.HORIZONTAL_SCROLLBAR | Manager.HORIZONTAL_SCROLLBAR);
		vfm = new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL | VerticalFieldManager.VERTICAL_SCROLLBAR | VerticalFieldManager.HORIZONTAL_SCROLLBAR | VerticalFieldManager.HORIZONTAL_SCROLLBAR);
		screen.add(vfm);
		screen.setTitle("Log Screen: " + name);
	}

	protected void writeScreen(String message, final int fg, final int bg, final boolean bold) {
		final LabelField label = new LabelField(message, LabelField.USE_ALL_WIDTH | LabelField.FIELD_LEFT | LabelField.FOCUSABLE) {
			public void paint(Graphics g) {
				try {
					if (bold) {
						setFont(FontFamily.forName("BBAlpha Serif").getFont(Font.BOLD, 12));
					} else {
						setFont(FontFamily.forName("BBAlpha Serif").getFont(Font.PLAIN, 12));
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				g.setBackgroundColor(bg);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(fg);
				g.clear();
				super.paint(g);
			}
		};
		vfm.add(label);
	}

	public void debug(String message) {
		message = "[DEBUG] " + simpleDateFormat.format(new Date()) + ": " + message;
		writeScreen(message, Color.BLACK, Color.WHITE, false);
	}

	public void info(String message) {
		message = "[INFO] " + simpleDateFormat.format(new Date()) + ": " + message;
		writeScreen(message, Color.GREEN, Color.WHITE, true);
	}

	public void warn(String message) {
		message = "[WARN] " + simpleDateFormat.format(new Date()) + ": " + message;
		writeScreen(message, Color.ORANGE, Color.WHITE, true);
	}

	public void error(String message) {
		message = "[ERROR] " + simpleDateFormat.format(new Date()) + ": " + message;
		writeScreen(message, Color.RED, Color.WHITE, true);
	}

	public void fatal(String message) {
		message = "[FATAL] " + simpleDateFormat.format(new Date()) + ": " + message;
		writeScreen(message, Color.RED, Color.BLACK, true);
	}

	public void close() {
		screen = null;
		vfm = null;
	}

	public MainScreen getLogScreen() {
		return screen;
	}

}