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

import java.util.Hashtable;
import java.util.Vector;

/**
 * CookieManager
 * 
 * Used to save and retrieve cookies set for a given domain
 * Modeled after: http://www.hccp.org/java-net-cookie-how-to.html
 * 
 * @author Eki Baskoro
 * @version 0.1
 *  
 */
public class CookieManager
{

	/**
	 * Cookies are delimited by ';'
	 */
    private static String COOKIE_VALUE_DELIMITER = ";";
    
    /**
     * Values are delimited by ','
     */
    private static String COMMA = ", ";
    
    /**
     * Expiration key.
     */
    private static String EXPIRES = "expires";
    
    /**
     * Storage for cookies.
     */
    private Hashtable cookieJar;

    /**
     * Default constructor.
     * 
     */
    public CookieManager() {
        cookieJar = new Hashtable();
    }

    /**
     * Saves cookies to a Hashtable: <String,Vector> = <domain, cookies>
     * 
     * @param setCookieValue - comma separated list of HTTP cookies (value of set-cookie HTTP response header)
     * @param host - used to retrieve the domain of the cookie(s)
     */
    public void storeCookies(String setCookieValue, String host) {
        String domain = getDomainFromHost(host);
        
        //set-cookie header may contain multiple cookie definition
        //The following method separates these cookies into a single Vector collection
        Vector cookies = parseSetCookie(setCookieValue);
        
        if (cookies == null)
        	return;
        
        int size = cookies.size();
        
        for (int x = 0; x < size; x++) {
            //Extract the cookie name-value pair from the full cookie string:
            String sCookieValue = cookies.elementAt(x).toString();
            int indexEnd = sCookieValue.indexOf(COOKIE_VALUE_DELIMITER);
            
            if (indexEnd >= 0) {
                sCookieValue = sCookieValue.substring(0, indexEnd);
            }
           
           //Check to see the Hashtable already contains cookies for the given domain:
            Vector cookieVector;
            
            if (cookieJar.containsKey(domain)) {
                cookieVector = (Vector)cookieJar.get(domain);
            } else {
                cookieVector = new Vector();
            }
            
            //Check to see if this cookie already exists in the Hashtable
            if (cookieVector.contains(sCookieValue)) {
            } else {
                cookieVector.addElement(sCookieValue);
            }
            
            //persist to memory
            cookieJar.put(domain, cookieVector);
        }
    }

    /**
     * Retrieves list of cookies for a given domain
     * @param host - used to retrieve the domain of the cookie(s)
     * @return - semi-colon-delimited list of cookie name-value pairs
     */
    public String getCookies(String host) {
        StringBuffer sbCookies = new StringBuffer();
        String domain = getDomainFromHost(host);
        
        if (cookieJar.containsKey(domain)) {
            Vector cookieVector = (Vector)cookieJar.get(domain);
            
            if (cookieVector != null) {
                if (!cookieVector.isEmpty()) {
                    int size = cookieVector.size();
                    int counter = 1;
                    
                    for (int x = 0; x < size; x++) {
                        sbCookies.append((String) cookieVector.elementAt(x));
                        
                        if (x < (size - 1)) {
                            sbCookies.append(COOKIE_VALUE_DELIMITER);
                            counter ++;
                        }
                    }
                }
            }
        }
        
        return sbCookies.toString();
    }

    //-----------------------------------------------------------------------------
    //  separates multiple cookies defined in a set-cookie HTTP response header into a Vector collection
    //  format: cookie_name=value; domain=[domain]; path=[path];expires=[expires], cookie_name2=value; ...
    //
    //TODO: currently this method is only returning the name-value pair; make it more robust by modifying it to return all properties for a cookie
    //
    public Vector parseSetCookie(String cookie) {
        Vector cookieVector = new Vector();
        final int indexStart = 0;
        boolean isLastCookie = false;
    
        while (!isLastCookie) {
            boolean isLastWord = false;
            
            while (!isLastWord) {
                int indexEnd = cookie.indexOf(COOKIE_VALUE_DELIMITER);
                
                if (indexEnd >= 0) {
                    String cookieSegment = cookie.substring(indexStart, indexEnd);
                    
                    int startIndexOfComma = cookieSegment.indexOf(COMMA);
                    
                    if (startIndexOfComma > 0) {
                        //segment contains a comma: could either be the expires property, or a delimiter between two cookies
                        int endIndexOfComma = cookieSegment.lastIndexOf(COMMA.charAt(0));
                        
                        if (cookieSegment.toLowerCase().startsWith(EXPIRES)) {
                            //first comma detected is part of the Expires property, check to see if any more commas are found before the next ";"
                            if (startIndexOfComma < endIndexOfComma) {
                                //multiple commas - 2nd one is a delimiter between cookies
                                isLastWord = true;  //this is the last property of the current cookie
                                String lastWord = cookie.substring(0, endIndexOfComma);
                                
                                if (isCookieName(lastWord)) {
                                    cookieVector.addElement(lastWord + COOKIE_VALUE_DELIMITER);  //cookie name should never be at the end, but check anyway
                                }
                                
                                cookie = cookie.substring(endIndexOfComma + 1).trim();     //chop the  property off the working cookie value (including the "," that delimits the two cookies)
                            } else {
                                //only one comma found - for the expires property
                                cookie = cookie.substring(indexEnd + 1).trim();     //chop the Expires property off the working cookie value (including the ";")
                            }
                        } else {
                            //single comma found - delimiter between multiple cookies
                            isLastWord = true;  //this is the last property of the current cookie
                            String lastWord = cookie.substring(0, startIndexOfComma);
                            
                            if (isCookieName(lastWord)) {
                                cookieVector.addElement(lastWord + COOKIE_VALUE_DELIMITER);
                            }
                            
                            cookie = cookie.substring(startIndexOfComma + 1).trim();     //chop the  property off the working cookie value (including the "," that delimits the two cookies)
                        }
                    } else {
                        //not the expires property or the end & start of two cookies
                        if (isCookieName(cookieSegment)) {
                            cookieVector.addElement(cookieSegment + COOKIE_VALUE_DELIMITER);
                        }
                        
                        cookie = cookie.substring(indexEnd + 1).trim();     //chop the cookieSegment off the working cookie value (including the ";")
                    }

                } else {
                    //cookie is final segment in original string
                    if (isCookieName(cookie)) {
                        cookieVector.addElement(cookie + COOKIE_VALUE_DELIMITER);
                    }
                    
                    isLastWord = true;
                    isLastCookie = true;
                }
            }
        }
        
        //Vector containing a collection of [name=value] pairs representing a cookie.
        return cookieVector;
    }

    //Idenfities whether a segment of a cookie string is the name=value pair
    private boolean isCookieName(String cookieSegment) {       
        //check for invalid format 
        if (cookieSegment.equals("")) 
            return false;
        
        if (cookieSegment.indexOf("=") < 0) 
            return false;
        
        //check for supported cookie property keyword
        if (cookieSegment.toLowerCase().startsWith("domain")) 
            return false;
        
        if (cookieSegment.toLowerCase().startsWith(EXPIRES)) 
            return false;
        
        if (cookieSegment.toLowerCase().startsWith("comment")) 
            return false;
        
        if (cookieSegment.toLowerCase().startsWith("max-age")) 
            return false;
        
        if (cookieSegment.toLowerCase().startsWith("path")) 
            return false;
        
        if (cookieSegment.toLowerCase().startsWith("secure")) 
            return false;
        
        if (cookieSegment.toLowerCase().startsWith("version")) 
            return false;
        
        //the cookieSegment is the [name=value] piece of the set-cookie header
        return true;
    }
    
    private static String getDomainFromHost(String host) {
        if (host.indexOf('.') != host.lastIndexOf('.')) {
            return host.substring(host.indexOf('.') + 1);
        } else {
            return host;
        }
    }
    
}