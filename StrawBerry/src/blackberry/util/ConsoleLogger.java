package blackberry.util;

/*
Copyright (c) 2010 E.Y. Baskoro

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/

import java.util.Date;

import net.rim.device.api.i18n.SimpleDateFormat;

/**
 * ConsoleLogger
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public class ConsoleLogger implements Logger
{

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z");
	
	/**
	 * Logger's name.
	 */
	private String name;
	
	/**
	 * Create a logger given name.
	 * 
	 * @param name the name.
	 */
	ConsoleLogger(String name) {
		this.name = name;
	}
	
	/**
	 * Obtain the logger's name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see blackberry.util.Logger#debug(java.lang.String)
	 */
	public void debug(String message) {
		System.out.println("[DEBUG] " + simpleDateFormat.format(new Date()) + " " + name + ": " + message);
	}

	/*
	 * (non-Javadoc)
	 * @see blackberry.util.Logger#error(java.lang.String)
	 */
	public void error(String message) {
		System.out.println("[ERROR] " + simpleDateFormat.format(new Date()) + " " + name + ": " + message);

	}

}
