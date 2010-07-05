package blackberry.facebook;

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

import net.rim.device.api.util.Persistable;

/**
 * ApplicationSettings
 * 
 * Encapsulates the Facebook Application settings.
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public class ApplicationSettings implements Persistable
{
	
	/**
	 * Facebook REST server's URL.
	 */
	public String restUrl = null;
	
	/**
	 * Facebook Graph API server's URL.
	 */
	public String graphUrl = null;
	
	/**
	 * Next URL to denote success.
	 */
	public String nextUrl = null;
	
	/**
	 * Facebook Application Key.
	 */
	public String applicationKey = null;
	
	/**
	 * Facebook Application Secret.
	 */
	public String applicationSecret = null;
	
	/**
	 * Facebook Application (Client) ID.
	 */
	public long applicationId = 0L;
	
	/**
	 * Session key.
	 */
	public String sessionKey = null;
	
	/**
	 * Session secret.
	 * Can either be application secret or session secret.
	 */
	public String sessionSecret = null;
	
	/**
	 * Access token.
	 */
	public String accessToken = null;
	
	/**
	 * Constructor.
	 * 
	 * @param restUrl Facebook REST server's URL.
	 * @param graphUrl Facebook Graph API server's URL.
	 * @param nextUrl Next URL to denote success.
	 * @param applicationKey Facebook Application Key.
	 * @param applicationSecret Facebook Application Secret.
	 * @param applicationId Facebook Application (Client) ID.
	 */
	public ApplicationSettings(String restUrl, String graphUrl, String nextUrl, String applicationKey, String applicationSecret, long applicationId) {
		this.restUrl = restUrl;
		this.graphUrl = graphUrl;
		this.nextUrl = nextUrl;
		this.applicationKey = applicationKey;
		this.applicationSecret = applicationSecret;
		this.applicationId = applicationId;
	}
	
}
