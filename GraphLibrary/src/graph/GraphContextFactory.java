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

import java.util.Hashtable;

/**
 * GraphContextFactory
 * 
 * Factory for Graph context.
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public class GraphContextFactory implements GraphContext
{

	/**
	 * Holds current implementation instance of factory.
	 */
	private GraphContextFactory impl = null;
	
	/**
	 * Provider's URL.
	 */
	protected String providerUrl = null;
	
	/**
	 * Access token.
	 */
	protected String accessToken = null;

	/**
	 * Default constructor.
	 * 
	 * @throws GraphException when any error occurs.
	 */
	public GraphContextFactory() throws GraphException {
		String contextFactoryClassName = System.getProperty(CONTEXT_FACTORY);

		if (contextFactoryClassName == null)
			throw new GraphException("Missing Graph context factory property");

		try {
			if (!getClass().isAssignableFrom(Class.forName(contextFactoryClassName)))
				throw new GraphException("Invalid Graph context factory class");
		} catch (ClassNotFoundException e) {
			throw new GraphException("Graph context factory class not found");
		}

		providerUrl = System.getProperty(PROVIDER_URL);

		if (providerUrl == null)
			throw new GraphException("Missing Graph provider URL property");

		try {
			impl = (GraphContextFactory)Class.forName(contextFactoryClassName).newInstance();
			impl.setProviderUrl(providerUrl);
		} catch (Exception e) {
			throw new GraphException(e.getMessage());
		}
	}

	/**
	 * Create a factory instance.
	 * 
	 * @param env the environment variables.
	 * @throws GraphException when any error occurs.
	 */
	public GraphContextFactory(Hashtable env) throws GraphException {
		Object contextFactoryClassName = env.get(CONTEXT_FACTORY);

		if (contextFactoryClassName == null)
			throw new GraphException("Missing Graph context factory property");

		if (!(contextFactoryClassName instanceof String))
			throw new GraphException("Invalid Graph context factory property");

		try {
			if (!getClass().isAssignableFrom(Class.forName((String)contextFactoryClassName)))
				throw new GraphException("Invalid Graph context factory class");
		} catch (ClassNotFoundException e) {
			throw new GraphException("Graph context factory class not found");
		}

		Object providerUrlName = env.get(PROVIDER_URL);

		if (providerUrlName == null)
			throw new GraphException("Missing provider URL property");

		if (!(providerUrlName instanceof String))
			throw new GraphException("Invalid provider URL property");

		try {
			providerUrl = (String)providerUrlName;
			impl = (GraphContextFactory)Class.forName((String)contextFactoryClassName).newInstance();
			impl.setProviderUrl(providerUrl);
		} catch (Exception e) {
			throw new GraphException(e.getMessage());
		}
	}
	
	/**
	 * Dummy constructor.
	 * 
	 * @param constructed true when implementation has been instantiated, false otherwise.
	 */
	protected GraphContextFactory(boolean constructed) {
	}

	/*
	 * (non-Javadoc)
	 * @see graph.GraphContext#getAccessToken()
	 */
	public String getAccessToken() {
		return impl.getAccessToken();
	}

	/*
	 * (non-Javadoc)
	 * @see graph.GraphContext#setAccessToken(java.lang.String)
	 */
	public void setAccessToken(String accessToken) {
		impl.setAccessToken(accessToken);
	}

	/*
	 * (non-Javadoc)
	 * @see graph.GraphContext#read(java.lang.String)
	 */
	public Object read(String path) throws GraphException {
		return impl.read(path);
	}

	/*
	 * (non-Javadoc)
	 * @see graph.GraphContext#read(java.lang.String, graph.Parameters)
	 */
	public Object read(String path, Parameters params) throws GraphException {
		return impl.read(path, params);
	}

	/*
	 * (non-Javadoc)
	 * @see graph.GraphContext#write(java.lang.String, java.lang.Object)
	 */
	public Object write(String path, Object object) throws GraphException {
		return impl.write(path, object);
	}
	
	/*
	 * (non-Javadoc)
	 * @see graph.GraphContext#delete(java.lang.String)
	 */
	public Object delete(String path) throws GraphException {
		return impl.delete(path);
	}

	/**
	 * Obtain the provider's URL.
	 * 
	 * @return the URL.
	 */
	public String getProviderUrl() {
		return providerUrl;
	}
	
	/**
	 * Change the provider's URL.
	 * 
	 * @param providerUrl the new URL.
	 */
	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

}
