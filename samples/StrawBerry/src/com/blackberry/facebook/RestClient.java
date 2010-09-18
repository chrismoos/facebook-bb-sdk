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
package com.blackberry.facebook;

import java.io.IOException;
import java.util.Hashtable;

import net.rim.device.api.collection.util.SortedReadableList;
import net.rim.device.api.crypto.MD5Digest;
import net.rim.device.api.util.StringComparator;

import com.blackberry.facebook.json.JSONObject;
import com.blackberry.facebook.json.JSONTokener;
import com.blackberry.util.network.HttpClient;
import com.blackberry.util.network.HttpConnectionFactory;

public class RestClient {

	/**
	 * URL to REST server.
	 */
	private String url = null;

	/**
	 * Facebook Application Key.
	 */
	private String applicationKey = null;

	/**
	 * Session key.
	 */
	private String sessionKey = null;

	/**
	 * Secret key. Can either be the Facebook Application secret or the session secret.
	 */
	private String secretKey = null;

	private HttpConnectionFactory factory;

	private HttpClient httpClient;

	/**
	 * Default constructor.
	 * 
	 */
	public RestClient(HttpConnectionFactory pFactory) {
		factory = pFactory;
		httpClient = new HttpClient(factory);
	}

	/**
	 * Create a REST client given its URL.
	 * 
	 * @param url
	 *            the URL to the REST server.
	 */
	public RestClient(String url, HttpConnectionFactory pFactory) {
		this.url = url;
		factory = pFactory;
		httpClient = new HttpClient(factory);
	}

	/**
	 * Create a REST client given its URL and application key.
	 * 
	 * @param url
	 *            the URL to the REST server.
	 * @param applicationKey
	 *            the application key.
	 */
	public RestClient(String url, String applicationKey, HttpConnectionFactory pFactory) {
		this.url = url;
		this.applicationKey = applicationKey;
		factory = pFactory;
		httpClient = new HttpClient(factory);
	}

	/**
	 * Create a REST client given its URL and application key.
	 * 
	 * @param url
	 *            the URL to the REST server.
	 * @param applicationKey
	 *            the application key.
	 * @param secretKey
	 *            the secret key used to calculate signature.
	 */
	public RestClient(String url, String applicationKey, String secretKey, HttpConnectionFactory pFactory) {
		this.url = url;
		this.applicationKey = applicationKey;
		this.secretKey = secretKey;
		factory = pFactory;
		httpClient = new HttpClient(factory);
	}

	/**
	 * Obtain the REST server's URL.
	 * 
	 * @return the URL.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Change the REST server's URL.
	 * 
	 * @param url
	 *            the new URL.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Obtain the application key.
	 * 
	 * @return the application key.
	 */
	public String getApplicationKey() {
		return applicationKey;
	}

	/**
	 * Change the application key.
	 * 
	 * @param applicationKey
	 *            the application key.
	 */
	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}

	/**
	 * Obtain the session key.
	 * 
	 * @return the session key.
	 */
	public String getSessionKey() {
		return sessionKey;
	}

	/**
	 * Change the session key.
	 * 
	 * @param sessionKey
	 *            the new session key.
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * Obtain the secret key used to calculate the signature.
	 * 
	 * @return the secret key.
	 */
	public String getSecretKey() {
		return secretKey;
	}

	/**
	 * Change the secret key used to calculate the signature.
	 * 
	 * @param secretKey
	 *            the new secret key.
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * Send request to the REST server given data.
	 * 
	 * @param data
	 *            the data to send.
	 * @return response from the REST server as a JSON object.
	 * @throws RestException
	 *             when encounters any error.
	 */
	public JSONObject send(Hashtable data) throws RestException {
		try {
			if (sessionKey != null) {
				data.put("session_key", sessionKey);
			}

			data.put("api_key", applicationKey);
			data.put("call_id", String.valueOf(System.currentTimeMillis()));
			data.put("v", "1.0");
			data.put("format", "JSON");
			data.put("sig", getSignature(data, secretKey));

			StringBuffer response = httpClient.doPost(url, data);

			if (response.length() == 0) {
				throw new RestException("Empty response");
			}

			return new JSONObject(new JSONTokener(response.toString()));
		} catch (RestException e) {
			throw e;
		} catch (Exception e) {
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Calculate the signature given arguments and secret key.
	 * 
	 * @param arguments
	 *            the supplied arguments.
	 * @param secret
	 *            the secret key.
	 * @return the signature string.
	 */
	private static String getSignature(Hashtable arguments, String secret) {
		try {
			SortedReadableList keysList = new SortedReadableList(StringComparator.getInstance(true));
			keysList.loadFrom(arguments.keys());
			keysList.sort();
			StringBuffer requestString = new StringBuffer();

			for (int i = 0; i < keysList.size(); i++) {
				String key = (String) keysList.getAt(i);
				String val = (String) arguments.get(key);
				requestString.append(key + "=" + val);
			}

			requestString.append(secret);

			MD5Digest digest = new MD5Digest();
			digest.update(requestString.toString().getBytes("iso-8859-1"), 0, requestString.length());
			byte[] digestResult = digest.getDigest();

			return convertToHex(digestResult);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Convert binary data to its Hex string.
	 * 
	 * @param data
	 *            the binary data to convert.
	 * @return the equivalent Hex string.
	 */
	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;

			do {
				if ((0 <= halfbyte) && (halfbyte <= 9)) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - 10)));
				}

				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}

		return buf.toString();
	}

}
