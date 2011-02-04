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

import java.util.Enumeration;
import java.util.Vector;

public abstract class AbstractLogger implements ChainLoggable, AppenderAttachable, Controllable {

	protected String name;
	protected int level;
	protected boolean additive;
	protected ChainLoggable parent;

	protected Vector appendersVector;

	public AbstractLogger(String pName, int pLevel, boolean pAdditive, Appender[] pAppenders) {
		this(pName, pLevel, pAdditive, arrayToVector(pAppenders));
	}

	public AbstractLogger(String pName, int pLevel, boolean pAdditive, Vector pAppenders) {
		name = pName;
		level = pLevel;
		additive = pAdditive;
		appendersVector = pAppenders;
	}

	protected static Vector arrayToVector(Object[] a) {
		Vector v = new Vector();

		if ((a != null) && (a.length > 0)) {
			for (int i = 0; i < a.length; i++) {
				v.addElement(a[i]);
			}
		}

		return v;
	}

	protected Appender[] vectorToArray(Vector v) {
		Appender[] a = null;

		if ((v != null) && (v.size() > 0)) {
			a = new Appender[v.size()];
			v.copyInto(a);
		}

		return a;
	}

	// implements ChainLoggable

	public String getName() {
		return name;
	}

	public void setName(String pName) {
		name = pName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int pLevel) {
		level = pLevel;
	}

	public boolean getAdditive() {
		return additive;
	}

	public void setAdditive(boolean pAdditive) {
		additive = pAdditive;
	}

	public ChainLoggable getParent() {
		return parent;
	}

	public void setParent(ChainLoggable pParent) {
		parent = pParent;
	}

	public abstract void debug(String message);

	public abstract void error(String message);

	public abstract void fatal(String message);

	public abstract void info(String message);

	public abstract void warn(String message);

	// implements AppenderAttachable

	public Appender[] getAppenders() {
		return vectorToArray(appendersVector);
	}

	public void addAppender(Appender newAppender) {
		if (appendersVector != null) {
			appendersVector.addElement(newAppender);
		}
	}

	public Enumeration getAllAppenders() {
		return appendersVector.elements();
	}

	public Appender getAppender(String name) {
		Appender a = null;
		Enumeration e = appendersVector.elements();

		while (e.hasMoreElements()) {
			Appender b = (Appender) e.nextElement();
			if ((b != null) && b.getName().trim().equals(name)) {
				return b;
			}
		}

		return a;
	}

	public boolean isAttached(Appender appender) {
		return appendersVector.contains(appender);
	}

	public void removeAllAppenders() {
		appendersVector.removeAllElements();
	}

	public void removeAppender(Appender appender) {
		appendersVector.removeElement(appender);
	}

	public void removeAppender(String name) {
		Enumeration e = appendersVector.elements();

		while (e.hasMoreElements()) {
			Appender b = (Appender) e.nextElement();
			if ((b != null) && b.getName().trim().equals(name)) {
				appendersVector.removeElement(b);
			}
		}
	}

	// implements Controllable

	public abstract void close();

	public abstract void reset();

	public abstract void clear();

	public abstract void show();

}
