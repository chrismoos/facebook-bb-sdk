package com.blackberry.util.log;

public interface Loggable {

	Logger console = LoggerFactory.getLogger("CONSOLE");
	Logger log = LoggerFactory.getLogger("TEXT");

}