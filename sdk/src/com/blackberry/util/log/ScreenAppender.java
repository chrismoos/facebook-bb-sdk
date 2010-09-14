/**
 * Copyright (c) E.Y. Baskoro, Research In Motion Limited.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * This License shall be included in all copies or substantial 
 * portions of the Software.
 * 
 * The name(s) of the above copyright holders shall not be used 
 * in advertising or otherwise to promote the sale, use or other 
 * dealings in this Software without prior written authorization.
 * 
 */
package com.blackberry.util.log;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;

public class ScreenAppender extends AbstractAppender {

	protected LogScreen screen;

	public ScreenAppender(String pName, String pType, int pThreshold, String pDestination) {
		super(pName, pType, pThreshold, pDestination);
		screen = new LogScreen(pName);
	}

	public void writeLine(int level, String message, final int fg, final int bg, final boolean bold) {
		final LogEntryField label = new LogEntryField(message, level) {
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
		screen.addLog(label);
	}

	public void close() {
		if (screen != null) {
			screen.clearAll();
		}
		screen = null;
	}

	public void show() {
		UiApplication.getUiApplication().pushScreen(screen);
	}

	public LogScreen getLogScreen() {
		return screen;
	}

	public void clear() {
		if (screen != null) {
			screen.clearAll();
		}
	}

}