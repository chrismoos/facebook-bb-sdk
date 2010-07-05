package graph;

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

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Parameters
 * 
 * Represents a collection of parameters.
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public class Parameters
{

	/**
	 * Stores parameters.
	 */
	private Hashtable parameters = new Hashtable();

	/**
	 * Default constructor.
	 * 
	 */
	public Parameters() {
	}

	/**
	 * Add a parameter to the collection.
	 * 
	 * @param name the parameter's name.
	 * @param value the parameter's value.
	 * @return this collection instance.
	 */
	public Parameters add(String name, String value) {
		parameters.put(name, new Parameter(name, value));

		return this;
	}

	/**
	 * Add a parameter instance to the collection.
	 * 
	 * @param parameter the parameter's instance.
	 * @return this collection instance.
	 */
	public Parameters add(Parameter parameter) {
		parameters.put(parameter.getName(), parameter);

		return this;
	}

	/**
	 * Obtain a parameter from the collection.
	 * 
	 * @param name the parameter's name.
	 * @return an instance of parameter.
	 */
	public Parameter get(String name) {
		try {
			return (Parameter)parameters.get(name);
		} catch (Exception e) {}

		return null;
	}

	/**
	 * Obtain the number of parameters in the collection.
	 * 
	 * @return the count.
	 */
	public int getCount() {
		return parameters.size();
	}

	/**
	 * Obtain the enumeration of parameter names.
	 * 
	 * @return the enumeration.
	 */
	public Enumeration getParameterNames() {
		return parameters.keys();
	}

	/**
	 * Clear the collection.
	 * 
	 */
	public void clear() {
		parameters.clear();
	}
	
}
