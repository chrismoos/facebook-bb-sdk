package blackberry.net;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import net.rim.blackberry.api.browser.URLEncodedPostData;

/**
 * HttpClient
 * 
 * A simple HTTP client.
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public class HttpClient
{

	/**
	 * Perform GET operation.
	 * 
	 * @param url the GET URL.
	 * @return response.
	 * @throws Exception when any error occurs.
	 */
	public static StringBuffer doGet(String url, Hashtable args) throws Exception {
		StringBuffer urlBuffer = new StringBuffer(url);
		
		if (args != null && args.size() > 0) {
			urlBuffer.append('?');
			Enumeration keysEnum = args.keys();
			
			while (keysEnum.hasMoreElements()) {
				String key = (String)keysEnum.nextElement();
				String val = (String)args.get(key);
				urlBuffer.append(key).append('=').append(val);
				
				if (keysEnum.hasMoreElements()) {
					urlBuffer.append('&');
				}
			}
		}
		
		return doGet(urlBuffer.toString());
	}
	
	/**
	 * Perform GET operation.
	 * 
	 * @param url the GET URL.
	 * @return response.
	 * @throws Exception when any error occurs.
	 */
	public static StringBuffer doGet(String url) throws Exception {
		HttpConnection connection = null;
		StringBuffer buffer = new StringBuffer();
		
		try {
			connection = ConnectionFactory.getInstance().createConnection(url);
			
			switch (connection.getResponseCode()) {
			
				case HttpConnection.HTTP_OK: {
					InputStream inputStream = connection.openInputStream();
					int c;
					
					while ((c = inputStream.read()) != -1) {
						buffer.append((char)c);
					}
					
					inputStream.close();
					break;
				}
				
				case HttpConnection.HTTP_TEMP_REDIRECT:
				case HttpConnection.HTTP_MOVED_TEMP:
				case HttpConnection.HTTP_MOVED_PERM: {
					url = connection.getHeaderField("Location");
					buffer = doGet(url);
					break;
				}
				
				default:
					break;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (connection != null) {
				try { connection.close(); }
				catch (IOException e) {}
			}
		}
		
		return buffer;
	}
	
	/**
	 * Perform POST operation.
	 * 
	 * @param url the POST url.
	 * @param data the data to be POSTed
	 * @return response.
	 * @throws Exception when any error occurs.
	 */
	public static StringBuffer doPost(String url, Hashtable data) throws Exception {
		URLEncodedPostData encoder = new URLEncodedPostData(null, false);
		Enumeration keysEnum = data.keys();
		
		while (keysEnum.hasMoreElements()) {
			String key = (String)keysEnum.nextElement();
			String val = (String)data.get(key);
			encoder.append(key, val);
		}
		
		return doPost(url, encoder.getBytes());
	}
	
	/**
	 * Perform POST operation.
	 * 
	 * @param url the POST url.
	 * @param data the data to be POSTed
	 * @return response.
	 * @throws Exception when any error occurs.
	 */
	public static StringBuffer doPost(String url, byte[] data) throws Exception {
		HttpConnection connection = null;
		StringBuffer buffer = new StringBuffer();
		
		try {
			connection = ConnectionFactory.getInstance().createConnection(url, null, data);
			
			switch (connection.getResponseCode()) {
			
				case HttpConnection.HTTP_OK: {
					InputStream inputStream = connection.openInputStream();
					int c;
					
					while ((c = inputStream.read()) != -1) {
						buffer.append((char)c);
					}
					
					inputStream.close();
					break;
				}
				
				case HttpConnection.HTTP_TEMP_REDIRECT:
				case HttpConnection.HTTP_MOVED_TEMP:
				case HttpConnection.HTTP_MOVED_PERM: {
					url = connection.getHeaderField("Location");
					buffer = doPost(url, data);
					break;
				}
				
				default:
					break;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (connection != null) {
				try { connection.close(); }
				catch (IOException e) {}
			}
		}
		
		return buffer;
	}
	
}
