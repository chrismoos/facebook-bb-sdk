package com.blackberry.util.log;

import net.rim.device.api.i18n.SimpleDateFormat;

public abstract class AbstractLogger implements Logger {

	protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

	public abstract void debug(String message);

	public abstract void error(String message);

	public abstract void fatal(String message);

	public abstract void info(String message);

	public abstract void warn(String message);

}
