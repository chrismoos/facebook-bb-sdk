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
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.io.http.HttpHeaders;
import net.rim.device.api.io.http.HttpProtocolConstants;
import net.rim.device.api.system.Branding;
import net.rim.device.api.system.DeviceInfo;

/**
 * ConnectionFactory
 * 
 * A singleton that returns HTTP connection.
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public final class ConnectionFactory
{

	/**
	 * Stores the singleton object.
	 */
	private static ConnectionFactory instance = null;
	
	/**
	 * Obtain the singleton object.
	 * 
	 * @return the singleton object.
	 */
	public static ConnectionFactory getInstance() {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		
		return instance;
	}
	
	/**
	 * The connection type.
	 */
	private int connectionType = 0;
	
	/**
	 * Default constructor.
	 * 
	 */
	private ConnectionFactory() {
		try {
			connectionType = Integer.parseInt(System.getProperty("blackberry.connection_type"));
		} catch (Exception e) {}
	}
	
	/**
	 * Obtain the connection type.
	 * 
	 * @return the connection type.
	 */
	public int getConnectionType() {
		return connectionType;
	}
	
	/**
	 * Change the connection type.
	 * 
	 * @param connectionType the new connection type.
	 */
	public void setConnectionType(int connectionType) {
		this.connectionType = connectionType;
	}
	
	/**
	 * Create a GET HTTP connection given URL.
	 * 
	 * @param url the URL to connect to.
	 * @return an instance of HTTP connection.
	 * @throws Exception when any error occurs.
	 */
	public HttpConnection createConnection(String url) throws Exception {
		return createConnection(url, null, null);
	}
	
	/**
	 * Create a GET HTTP connection given URL and headers.
	 * 
	 * @param url the URL to connect to.
	 * @param headers the headers to include with the request.
	 * @return an instance of HTTP connection.
	 * @throws Exception when any error occurs.
	 */
	public HttpConnection createConnection(String url, HttpHeaders headers) throws Exception {
		return createConnection(url, headers, null);
	}
	
	/**
	 * Create POST HTTP connection given URL, headers to include and data to POST.
	 * 
	 * @param url the URL to POST to.
	 * @param headers the headers to include with the request.
	 * @param data the data to be POSTed.
	 * @return an instance of HTTP connection.
	 * @throws Exception when any error occurs.
	 */
	public HttpConnection createConnection(String url, HttpHeaders headers, byte[] data) throws Exception {
		StringBuffer connectionUrl = new StringBuffer(url);
		HttpConnection connection = null;
		OutputStream outputStream = null;
		
		try {
			switch (connectionType) {
			
				case ConnectionType.BES: {
					connectionUrl.append(";deviceside=false");
					break;
				}
				
				case ConnectionType.DIRECT_TCP: {
					connectionUrl.append(";deviceside=true");
					break;
				}
				
				case ConnectionType.WIFI: {
					connectionUrl.append(";interface=wifi");
					break;
				}
				
				default:
					break;
			}
			
			connection = (HttpConnection)Connector.open(connectionUrl.toString());
			
			//add headers to connection
	        if (headers != null) {                
	            int size = headers.size();
	            
	            for (int i = 0; i < size;) {
	                String header = headers.getPropertyKey(i);
	                String value = headers.getPropertyValue(i ++);
	                
	                if (value != null) {
	                	connection.setRequestProperty(header, value);
	                }
	            }               
	        }
	        
	        if (data != null) {
	        	connection.setRequestMethod(HttpConnection.POST);
	        	connection.setRequestProperty(HttpProtocolConstants.HEADER_CONTENT_TYPE, HttpProtocolConstants.CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED);
	        	connection.setRequestProperty(HttpProtocolConstants.HEADER_CONTENT_LENGTH, String.valueOf(data.length));
	        	
	        	outputStream = connection.openOutputStream();
	        	outputStream.write(data);
	        } else {
	        	connection.setRequestMethod(HttpConnection.GET);
	        }
		} finally {
			if (outputStream != null) {
				try { outputStream.close(); }
				catch (IOException e) {}
			}
		}
		
		return connection;
	}
	
	/**
	 * Obtain the User-Agent header value.
	 * 
	 * @return the User-Agent header value.
	 */
	public static String getUserAgent() {
        StringBuffer sb = new StringBuffer();
        sb.append("BlackBerry");
        sb.append(DeviceInfo.getDeviceName());
        sb.append("/");
        sb.append(DeviceInfo.getSoftwareVersion());
        sb.append(" Profile/");
        sb.append(System.getProperty("microedition.profiles"));
        sb.append(" Configuration/");
        sb.append(System.getProperty("microedition.configuration"));
        sb.append(" VendorID/");
        sb.append(Branding.getVendorId());
        
        return sb.toString();
    }
    
	/**
	 * Obtain the Profile header value.
	 * 
	 * @return the Profile header value.
	 */
    public static String getProfile() {
        StringBuffer sb = new StringBuffer();
        sb.append("http://www.blackberry.net/go/mobile/profiles/uaprof/");
        sb.append(DeviceInfo.getDeviceName());
        sb.append("/");
        sb.append(DeviceInfo.getSoftwareVersion().substring(0, 3));   //RDF file format is 4.5.0.rdf (does not include build version)
        sb.append(".rdf");
        
        return sb.toString();
    }
	
}
