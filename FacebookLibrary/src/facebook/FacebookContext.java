package facebook;

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
 * FacebookContext
 * 
 * An abstraction of context for Facebook environment.
 * 
 * @author Eki Baskoro
 * @version 0.1
 * 
 */
public abstract class FacebookContext
{
	
	/**
	 * Singleton placeholder.
	 */
	private static FacebookContext instance = null;
	
	/**
	 * Obtain the current instance of the context.
	 * 
	 * @return context instance.
	 */
	public static FacebookContext getInstance() {
		return instance;
	}
	
	/**
	 * Change the current context instance.
	 * 
	 * @param context the new context instance.
	 */
	protected static void setInstance(FacebookContext context) {
		instance = context;
	}
	
	/**
	 * Create Facebook session from the given authorisation token. 
	 * 
	 * @param token the authorisation token.
	 * @throws FacebookException when any error occurs.
	 */
	public abstract void getSession(String token) throws FacebookException;
	
	/**
	 * Upgrade the current Facebook session to access token.
	 * 
	 * @throws FacebookException when any error occurs.
	 */
	public abstract void upgradeSession() throws FacebookException;
	
	/**
	 * Check whether the current context already has a session.
	 * 
	 * @return true if has session, false otherwise.
	 */
	public abstract boolean hasSession();
	
	/**
	 * Obtain the currently logged in Facebook user.
	 * 
	 * @return an instance of user.
	 * @throws FacebookException when any error occurs.
	 */
	public abstract User getLoggedInUser() throws FacebookException;
	
}
