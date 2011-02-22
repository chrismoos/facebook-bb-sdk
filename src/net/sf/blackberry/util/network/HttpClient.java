/**
 * Copyright (c) E.Y. Baskoro, Research In Motion Limited.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * This License shall be included in all copies or substantial 
 * portions of the Software.
 * 
 * The name(s) of the above copyright holders shall not be used 
 * in advertising or otherwise to promote the sale, use or other 
 * dealings in this Software without prior written authorization.
 * 
 */
package net.sf.blackberry.util.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;


import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.io.http.HttpProtocolConstants;
import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.io.transport.TransportInfo;
import net.sf.blackberry.util.log.Logger;

public class HttpClient {

	protected static HttpClient http;

	protected ConnectionFactory factory;
	protected Logger log;

	static {
		http = new HttpClient();
	}

	public static HttpClient getInstance() {
		return http;
	}

	protected HttpClient() {
		factory = new ConnectionFactory();
		log = Logger.getLogger(getClass());
	}

	public StringBuffer doGet(String url, Hashtable args) throws Exception {
		StringBuffer urlBuffer = new StringBuffer(url);

		if ((args != null) && (args.size() > 0)) {
			urlBuffer.append('?');
			Enumeration keysEnum = args.keys();

			while (keysEnum.hasMoreElements()) {
				String key = (String) keysEnum.nextElement();
				String val = (String) args.get(key);
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
	 * @param url
	 *            the GET URL.
	 * @return response.
	 * @throws Exception
	 *             when any error occurs.
	 */
	public StringBuffer doGet(String url) throws Exception {
		HttpConnection conn = null;
		StringBuffer buffer = new StringBuffer();

		try {
			if ((url == null) || url.equalsIgnoreCase("") || (factory == null)) {
				return null;
			}
			ConnectionDescriptor connd = factory.getConnection(url);
			String transportTypeName = TransportInfo.getTransportTypeName(connd.getTransportDescriptor().getTransportType());
			conn = (HttpConnection) connd.getConnection();

			log.debug("HTTP-GET (" + transportTypeName + "):  " + conn.getURL());
			int resCode = conn.getResponseCode();
			String resMessage = conn.getResponseMessage();
			log.debug("Response:  " + resCode + " " + resMessage);

			switch (resCode) {

			case HttpConnection.HTTP_OK: {
				InputStream inputStream = conn.openInputStream();
				int c;

				while ((c = inputStream.read()) != -1) {
					buffer.append((char) c);
				}

				inputStream.close();
				break;
			}

			case HttpConnection.HTTP_TEMP_REDIRECT:
			case HttpConnection.HTTP_MOVED_TEMP:
			case HttpConnection.HTTP_MOVED_PERM: {
				url = conn.getHeaderField("Location");
				buffer = doGet(url);
				break;
			}

			default:
				break;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e) {
				}
			}
		}

		log.debug("Body:  " + buffer.toString());
		return buffer;
	}

	public StringBuffer doPost(String url, Hashtable data) throws Exception {
		URLEncodedPostData encoder = new URLEncodedPostData("UTF-8", false);
		Enumeration keysEnum = data.keys();

		while (keysEnum.hasMoreElements()) {
			String key = (String) keysEnum.nextElement();
			String val = (String) data.get(key);
			encoder.append(key, val);
		}

		return doPost(url, encoder.getBytes());
	}

	/**
	 * Perform POST operation.
	 * 
	 * @param url
	 *            the POST url.
	 * @param data
	 *            the data to be POSTed
	 * @return response.
	 * @throws Exception
	 *             when any error occurs.
	 */
	public StringBuffer doPost(String url, byte[] data) throws Exception {
		HttpConnection conn = null;
		OutputStream os = null;
		StringBuffer buffer = new StringBuffer();

		try {
			if ((url == null) || url.equalsIgnoreCase("") || (factory == null)) {
				return null;
			}

			ConnectionDescriptor connd = factory.getConnection(url);
			String transportTypeName = TransportInfo.getTransportTypeName(connd.getTransportDescriptor().getTransportType());
			conn = (HttpConnection) connd.getConnection();

			if (conn != null) {
				try {
					// post data
					if (data != null) {
						conn.setRequestMethod(HttpConnection.POST);
						conn.setRequestProperty(HttpProtocolConstants.HEADER_CONTENT_TYPE, HttpProtocolConstants.CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED);
						conn.setRequestProperty(HttpProtocolConstants.HEADER_CONTENT_LENGTH, String.valueOf(data.length));

						os = conn.openOutputStream();
						os.write(data);
					} else {
						conn.setRequestMethod(HttpConnection.GET);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

				log.debug("HTTP-POST (" + transportTypeName + "):  " + conn.getURL());
				int resCode = conn.getResponseCode();
				String resMessage = conn.getResponseMessage();
				log.debug("Response:  " + resCode + " " + resMessage);

				switch (resCode) {

				case HttpConnection.HTTP_OK: {
					InputStream inputStream = conn.openInputStream();
					int c;

					while ((c = inputStream.read()) != -1) {
						buffer.append((char) c);
					}

					inputStream.close();
					break;
				}

				case HttpConnection.HTTP_TEMP_REDIRECT:
				case HttpConnection.HTTP_MOVED_TEMP:
				case HttpConnection.HTTP_MOVED_PERM: {
					url = conn.getHeaderField("Location");
					buffer = doPost(url, data);
					break;
				}

				default:
					break;
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e) {
				}
			}
		}

		log.debug("Body:  " + buffer.toString());
		return buffer;
	}

}
