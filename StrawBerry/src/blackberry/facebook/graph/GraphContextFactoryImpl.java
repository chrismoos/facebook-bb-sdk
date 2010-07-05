package blackberry.facebook.graph;

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

import org.json.me.JSONObject;
import org.json.me.JSONTokener;

import blackberry.net.HttpClient;
import blackberry.util.Logger;
import blackberry.util.LoggerFactory;

import graph.GraphContextFactory;
import graph.GraphException;
import graph.Parameters;

/**
 * GraphContextFactoryImpl
 * 
 * Implementation of Graph context factory.
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public class GraphContextFactoryImpl extends GraphContextFactory
{

	// Logger
	private static final Logger log = LoggerFactory.getLogger(GraphContextFactoryImpl.class.getName());
	
	/**
	 * Default constructor.
	 * 
	 */
	public GraphContextFactoryImpl() {
		super(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see graph.GraphContextFactory#getAccessToken()
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/*
	 * (non-Javadoc)
	 * @see graph.GraphContextFactory#setAccessToken(java.lang.String)
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/*
	 * (non-Javadoc)
	 * @see graph.GraphContextFactory#read(java.lang.String)
	 */
	public Object read(String path) throws GraphException {
		return read(path, null);
	}

	/*
	 * (non-Javadoc)
	 * @see graph.GraphContextFactory#read(java.lang.String, graph.Parameters)
	 */
	public Object read(String path, Parameters params) throws GraphException {
		Hashtable args = new Hashtable();
		args.put("access_token", accessToken);
		args.put("format", "JSON");
		
		if (params != null && params.getCount() > 0) {
			Enumeration paramNamesEnum = params.getParameterNames();
			
			while (paramNamesEnum.hasMoreElements()) {
				String paramName = (String)paramNamesEnum.nextElement();
				String paramValue = params.get(paramName).getValue();
				args.put(paramName, paramValue);
			}
		}
		
		try {
			StringBuffer responseBuffer = HttpClient.doGet(getProviderUrl() + '/' + path, args);

			if (responseBuffer.length() == 0)
				throw new Exception("Empty response");
			
			return new JSONObject(new JSONTokener(responseBuffer.toString()));
		} catch (Exception e) {
			throw new GraphException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see graph.GraphContextFactory#write(java.lang.String, java.lang.Object)
	 */
	public Object write(String path, Object object) throws GraphException {
		Hashtable data = new Hashtable();
		data.put("access_token", accessToken);
		data.put("format", "JSON");
		
		try {
			JSONObject jsonObject = (JSONObject)object;
			Enumeration keysEnum = jsonObject.keys();
			
			while (keysEnum.hasMoreElements()) {
				String key = (String)keysEnum.nextElement();
				Object val = jsonObject.get(key);
				
				if (!(val instanceof JSONObject)) {
					data.put(key, val.toString());
				}
			}
			
			StringBuffer responseBuffer = HttpClient.doPost(getProviderUrl() + '/' + path, data);
			
			if (responseBuffer.length() == 0)
				throw new GraphException("Empty response");
			
			return new JSONObject(new JSONTokener(responseBuffer.toString()));
		} catch (Exception e) {
			throw new GraphException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see graph.GraphContextFactory#delete(java.lang.String)
	 */
	public Object delete(String path) throws GraphException {
		Hashtable data = new Hashtable();
		data.put("access_token", accessToken);
		data.put("format", "JSON");
		data.put("method", "delete");
		
		try {
			StringBuffer responseBuffer = HttpClient.doPost(getProviderUrl() + '/' + path, data);
			
			if (responseBuffer.length() == 0)
				throw new GraphException("Empty response");
			
			return new JSONObject(new JSONTokener(responseBuffer.toString()));
		} catch (Exception e) {
			throw new GraphException(e.getMessage());
		}
	}

}
