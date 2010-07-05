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

/**
 * ExtendedPermission
 * 
 * Contains a collection of extended permission constants.
 * 
 * @author Eki Baskoro
 * @version 0.1
 * 
 */
public interface ExtendedPermission
{

	/**
	 * Access Emails.
	 */
	public static final String EMAIL = "email";
	
	/**
	 * Read streams.
	 */
	public static final String READ_STREAM = "read_stream";
	
	/**
	 * Publish streams.
	 */
	public static final String PUBLISH_STREAM = "publish_stream";
	
	/**
	 * Access account on behalf.
	 */
	public static final String OFFLINE_ACCESS = "offline_access";
	
	/**
	 * Facebook Chat access.
	 */
	public static final String XMPP_LOGIN = "xmpp_login";
	
}
