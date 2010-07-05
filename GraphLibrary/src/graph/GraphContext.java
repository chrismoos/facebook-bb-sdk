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

/**
 * GraphContext
 * 
 * A context for Graph API.
 * 
 * @author Eki Baskoro
 * @version 0.1
 * 
 */
public interface GraphContext
{

	/**
	 * Context factory property name.
	 */
	public static final String CONTEXT_FACTORY = "graph.context.factory";
	
	/**
	 * Context provider URL.
	 */
	public static final String PROVIDER_URL = "graph.provider.url";

	/**
	 * Obtain the access token.
	 * 
	 * @return the token.
	 */
	public String getAccessToken();
	
	/**
	 * Change the access token.
	 * 
	 * @param accessToken the new token.
	 */
	public void setAccessToken(String accessToken);

	/**
	 * Read an object from the current Graph context.
	 * 
	 * @param path the Graph path.
	 * @return the object.
	 * @throws GraphException when any error occurs.
	 */
	public Object read(String path) throws GraphException;
	
	/**
	 * Read an object from the current Graph context specifying some parameters.
	 * 
	 * @param path the Graph path.
	 * @param params the parameters.
	 * @return the object.
	 * @throws GraphException when any error occurs.
	 */
	public Object read(String path, Parameters params) throws GraphException;

	/**
	 * Write an object to the current Graph context.
	 * 
	 * @param path the Graph path.
	 * @param object the object to write.
	 * @return the write result.
	 * @throws GraphException when any error occurs.
	 */
	public Object write(String path, Object object) throws GraphException;
	
	/**
	 * Delete an object from the current Graph context.
	 * 
	 * @param path the Graph path.
	 * @return the delete result.
	 * @throws GraphException
	 */
	public Object delete(String path) throws GraphException;
	
}
