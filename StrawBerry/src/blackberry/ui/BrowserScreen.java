package blackberry.ui;

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
import java.util.Vector;

import javax.microedition.io.HttpConnection;

import blackberry.net.ConnectionFactory;
import blackberry.net.CookieManager;

import net.rim.device.api.browser.field.BrowserContent;
import net.rim.device.api.browser.field.BrowserContentChangedEvent;
import net.rim.device.api.browser.field.Event;
import net.rim.device.api.browser.field.ExecutingScriptEvent;
import net.rim.device.api.browser.field.RedirectEvent;
import net.rim.device.api.browser.field.RenderingApplication;
import net.rim.device.api.browser.field.RenderingException;
import net.rim.device.api.browser.field.RenderingOptions;
import net.rim.device.api.browser.field.RenderingSession;
import net.rim.device.api.browser.field.RequestedResource;
import net.rim.device.api.browser.field.SetHeaderEvent;
import net.rim.device.api.browser.field.SetHttpCookieEvent;
import net.rim.device.api.browser.field.UrlRequestedEvent;
import net.rim.device.api.io.http.HttpHeaders;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.Status;

/**
 * BrowserScreen
 * 
 * Represents a programmable browser. The browser is a screen that will give an
 * outcome of "success" when successfully browsed to the specified URL.
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public class BrowserScreen extends AbstractScreen implements RenderingApplication
{
	
	/**
	 * Manages the rendering session.
	 */
    private RenderingSession renderingSession;
    
    /**
     * Manages the current HTTP connection.
     */
    private HttpConnection currentConnection;
    
    /**
     * Manages cookies.
     */
    private CookieManager cookieManager;
    
    /**
     * The URL to browse for.
     */
    private String url;
    
    /**
     * Home menu brings back to the initial URL.
     * 
     * TODO: Localise this.
     */
    private  final MenuItem HOME_MENU_ITEM = new MenuItem("Home", Integer.MAX_VALUE, 1)
    {
        
    	public void run() {
            requestURL(url, url);
        }
    	
    };
    
    /**
     * Exit menu cancels the browsing.
     * 
     * TODO: Localise this.
     */
    private final MenuItem EXIT_MENU_ITEM = new MenuItem("Exit", Integer.MAX_VALUE, 0)
    {
    	
        public void run() {
        	fireActioned("cancelled");
        }
        
    };

    /**
     * Default constructor.
     * 
     */
    public BrowserScreen() {
    	init();
    }
    
    /**
     * Constructor
     * 
     * @param url is the URL to open.
     */
    public BrowserScreen(String url) {
    	this.url = url;
        this.cookieManager = new CookieManager();
        
        init();
    }
    
    /**
     * Constructor
     * 
     * Instantiate an object.
     * 
     * @param url is the URL to open.
     * @param cookieManager is the cookie manager.
     */
    public BrowserScreen(String url, CookieManager cookieManager) {
    	this.url = url;
    	this.cookieManager = cookieManager;
    	
    	init();
    }
    
    /**
     * Obtain the URL to browse.
     * 
     * @return the URL.
     */
    public String getUrl() {
    	return url;
    }
    
    /**
     * Change the URL to browse.
     * 
     * @param url the new URL.
     */
    public void setUrl(String url) {
    	this.url = url;
    }
    
    /**
     * Browse to the URL.
     * 
     */
    public void browse() {
    	if (url != null) {
    		requestURL(url, url);
    	}
    }

    /**
     * Obtain the cookie manager.
     * 
     * @return the cookie manager.
     */
    public CookieManager getCookieManager() {
    	return cookieManager;
    }
    
    /**
     * Change the cookie manager.
     * 
     * @param cookieManager the cookie manager.
     */
    public void setCookieManager(CookieManager cookieManager) {
    	this.cookieManager = cookieManager;
    }
    
    protected void makeMenu(Menu menu, int instance) {
        menu.deleteAll();         //removes any contextual menu items (i.e. the "getLink" menu item added within the BrowserField).
        menu.add(HOME_MENU_ITEM);
        menu.add(EXIT_MENU_ITEM);
    }
    
    /**
     * Initialisation.
     * 
     */
    private void init() {
        //demonstrates how you can add a listener for key-press events:
        addKeyListener(new KeyListener()
        {
        	
            public boolean keyChar(char key, int status, int time) { return false; }
            
            public boolean keyDown(int keycode, int time) { return false; }
            
            public boolean keyUp(int keycode, int time) { return false; }
            
            public boolean keyRepeat(int keycode, int time) { return false; }
            
            public boolean keyStatus(int keycode, int time) { return false; }
            
        });

        //Initialize the rendering session (BrowserField)
        renderingSession = RenderingSession.getNewInstance();
        setRenderingOptions();
    }
    
    /**
     * Set rendering options.
     * 
     */
    private void setRenderingOptions() {
        renderingSession.getRenderingOptions().setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.JAVASCRIPT_ENABLED, true);
        renderingSession.getRenderingOptions().setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.JAVASCRIPT_LOCATION_ENABLED, true);
        renderingSession.getRenderingOptions().setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.WAP_MODE, true);
        renderingSession.getRenderingOptions().setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.ENABLE_WML, true);
        renderingSession.getRenderingOptions().setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.ENABLE_EMBEDDED_RICH_CONTENT, true);
        renderingSession.getRenderingOptions().setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.ENABLE_CSS, true);
    }

    /**
     * Browse to the URL.
     * 
     * @param url the URL to browse to.
     * @param referrer the referrer URL.
     */
    private void requestURL(String url, String referrer) {
        //Set the referrer header if it exists.
        //  note in some cases, the referrer header should not be included in the request and will be null.
        HttpHeaders requestHeaders = null;
        
        if (referrer != null) {
            requestHeaders = new HttpHeaders();
            requestHeaders.setProperty("referer", referrer);
        }
        
        PrimaryResourceFetchThread thread = new PrimaryResourceFetchThread(url, requestHeaders, null, null);
        thread.start();
    }
    
    /**
     * Process the current connection.
     * 
     * @param connection the connection to process.
     * @param event the included event.
     */
    private void processConnection(HttpConnection connection, Event event) {
        // A new request is being made - cancel previous request.
        if (currentConnection != null) {
            try {
                currentConnection.close();
            } catch (IOException e) {
            }
        }
        
        currentConnection = connection;

        try {
            //Send the HTTP Request
            BrowserContent browserContent = null;
            browserContent = renderingSession.getBrowserContent(currentConnection, this, event);
            
            if (browserContent != null)  {
                Field field = browserContent.getDisplayableContent();
                
                //Process the HTTP Response
                String urlHost = currentConnection.getHost();
                String urlProtocol = currentConnection.getProtocol();
                String urlCurrent = urlProtocol + "://" + urlHost;

                for (int i = 0; currentConnection.getHeaderFieldKey(i) != null; i++) {
                	String headerKey = currentConnection.getHeaderFieldKey(i);
                    
                    if (headerKey.equalsIgnoreCase("set-cookie")) {
                        //Save cookie(s) for this set-cookie header to persistent storage container:
                        String cookie = currentConnection.getHeaderField(i);
                        cookieManager.storeCookies(cookie, urlHost);

                    } else if (headerKey.equalsIgnoreCase("content-location")) {
                        //Check to see if server has indicated content to be retrieved from a new location
                        String newLocation = currentConnection.getHeaderField(i);
                        
                        if (urlCurrent.equalsIgnoreCase(newLocation)) {
                            //ideal: no internal redirects have occurred.  Continue processing the response.
                        } else {
                            //Even though the server returned a 200 code, this is a redirect scenario.  Send request for new URL.
                            requestURL(newLocation, urlCurrent);
                            break;
                        }
                    }
                }

                if (field != null) {
                    synchronized (Application.getEventLock()) {
                        deleteAll();
                        add(field);
                    }
                }
                
                //BrowserContent won't render any of its associated content until the finishLoading method has been invoked:
                browserContent.finishLoading();

            }
            
        } catch (RenderingException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
            SecondaryResourceFetchThread.doneAddingImages();
        }
    }    

    /**
     * @see net.rim.device.api.browser.RenderingApplication#eventOccurred(net.rim.device.api.browser.Event)
     */
    public Object eventOccurred(Event event)  {
        int eventId = event.getUID();

        switch (eventId) {

            case Event.EVENT_URL_REQUESTED: {
                UrlRequestedEvent urlRequestedEvent = (UrlRequestedEvent)event;    
                String absoluteURL = urlRequestedEvent.getURL();
                urlRequestedEvent.getHeaders().removeProperties("referer");
                PrimaryResourceFetchThread thread = new PrimaryResourceFetchThread(absoluteURL, urlRequestedEvent.getHeaders(), urlRequestedEvent.getPostData(), event);
                thread.start();
                break;
            } 

            case Event.EVENT_BROWSER_CONTENT_CHANGED: {
                BrowserContentChangedEvent browserContentChangedEvent = (BrowserContentChangedEvent)event; 
            
                if (browserContentChangedEvent.getSource() instanceof BrowserContent) { 
                    BrowserContent browserField = (BrowserContent)browserContentChangedEvent.getSource(); 
                    url = browserField.getURL();
                    
                    Application.getApplication().invokeAndWait(new Runnable()
                    {
                    	
						public void run() {
							fireActioned("success");
						}
						
					});
                }
                
                break;                
            } 

            case Event.EVENT_REDIRECT: {
                RedirectEvent redirectEvent = (RedirectEvent)event;
                String referrer = redirectEvent.getSourceURL();
                
                switch (redirectEvent.getType()) {  
                                        
                    case RedirectEvent.TYPE_SINGLE_FRAME_REDIRECT:
                        // Show redirect message.
                        Application.getApplication().invokeAndWait(new Runnable()
                        {
                        	
                            public void run() {
                                Status.show("You are being redirected to a different page...");
                            }
                            
                        });
                        
                        break;
                    
                    case RedirectEvent.TYPE_JAVASCRIPT:
                        referrer = currentConnection.getURL();
                        break;
                    
                    case RedirectEvent.TYPE_META:
                        // MSIE and Mozilla don't send a Referer for META Refresh.
                        referrer = null;
                        break;
                    
                    case RedirectEvent.TYPE_300_REDIRECT: {
                        // MSIE, Mozilla, and Opera all send the original 
                        // request's Referer as the Referer for the new request.
                        Object eventSource = redirectEvent.getSource();
                        
                        if (eventSource instanceof HttpConnection) {
                            referrer = ((HttpConnection)eventSource).getRequestProperty("referer");
                        }
                        
                        break;
                    }
                }

                try {
                    Object eventSource = redirectEvent.getSource();
                    
                    if (eventSource instanceof HttpConnection) {
                        HttpConnection redirConn = (HttpConnection)eventSource;
                        String host = redirConn.getHost();
                        String headerKey = "";
                        
                        for (int i = 0; (headerKey = redirConn.getHeaderFieldKey(i)) != null; i ++) {
                            if (headerKey.equalsIgnoreCase("set-cookie")) {
                                String cookie = redirConn.getHeaderField(i);
                                cookieManager.storeCookies(cookie, host);
                            }
                        }
                    }
                } catch (IOException e) {
                } catch (Exception e) {
                }
                
                requestURL(redirectEvent.getLocation(), referrer);
                break;
            } 
            
            case Event.EVENT_CLOSE: {
                if (currentConnection != null) {
                    try {
                        currentConnection.close();
                    } catch (IOException e) {}
                }
                
                cookieManager = null;
                renderingSession = null;
                break;
            }
            
            case Event.EVENT_SET_HEADER: {
                SetHeaderEvent setHeaderEvent = (SetHeaderEvent)event;
                String value = setHeaderEvent.getValue();
                break;
            }
                
            case Event.EVENT_SET_HTTP_COOKIE: {
            	//"Cookie" Http header set by JavaScript - add to cookie store.
                try {
                    SetHttpCookieEvent setHttpCookieEvent = (SetHttpCookieEvent)event; 
                    cookieManager.storeCookies(setHttpCookieEvent.getCookie(), currentConnection.getHost()); 
                } catch (Exception e) {}
                
                break;
            }
            
            case Event.EVENT_EXECUTING_SCRIPT: {
            	ExecutingScriptEvent executingScriptEvent = (ExecutingScriptEvent)event;
            	break;
            }
            
            case Event.EVENT_HISTORY:           // No history support.
            case Event.EVENT_FULL_WINDOW:       // No full window support.
            case Event.EVENT_STOP:              // No stop loading support.
            default:
            	break;
        }

        return null;
    }

    /**
     * @see net.rim.device.api.browser.RenderingApplication#getAvailableHeight(net.rim.device.api.browser.BrowserContent)
     */
    public int getAvailableHeight(BrowserContent browserField) {
        // Field has full screen.
        return Display.getHeight();
    }

    /**
     * @see net.rim.device.api.browser.RenderingApplication#getAvailableWidth(net.rim.device.api.browser.BrowserContent)
     */
    public int getAvailableWidth(BrowserContent browserField) {
        // Field has full screen.
        return Display.getWidth();
    }

    /**
     * @see net.rim.device.api.browser.RenderingApplication#getHistoryPosition(net.rim.device.api.browser.BrowserContent)
     */
    public int getHistoryPosition(BrowserContent browserField) {
        // No history support.
        return 0;
    }
    

    /**
     * @see net.rim.device.api.browser.RenderingApplication#getHTTPCookie(java.lang.String)
     */
    public String getHTTPCookie(String url) {
        //Retrieves cookies associated with a provided URL. This method is triggered when JavaScript on a web page requests a cookie. 
        //The method does not send a cookie or communicate the cookie to the web server hosting the page.
        String sHost = getHostFromURL(url);
        
        return cookieManager.getCookies(sHost);
    }

    /**
     * @see net.rim.device.api.browser.RenderingApplication#getResource(net.rim.device.api.browser.RequestedResource,
     *      net.rim.device.api.browser.BrowserContent)
     *      
     *      Purpose: Retrieve page resources such as images (asyncronously if possible)
     *      This method must return a non-null HTTP connection when the referrer is null. 
     *      This call can be non-blocking if referrer is not null. 
     *      If the call is not blocking, referrer must be notified with the connection via referrer.resourceReady(HttpConnection) call.
     * 
     *      Requested resource objects get passed into a rendering application when the rendering session needs assistance     
     *      in rendering an off-page resource (e.g. an image)
     */
    public HttpConnection getResource(RequestedResource resource, BrowserContent referrer) {
        if (resource == null)
            return null;

        String url = resource.getUrl();

        // Check if this is cache-only request.
        if (resource.isCacheOnly()) {
            // Incomplete: No cache support (need to check cache for resource and return that, otherwise)
            // If RequestedResource.isCacheOnly() is true then the content should only be returned if it is cached.
            return null;
        }
        
        if (url == null)
            return null;

        //Any requests for external resources should include the same headers sent for normal web requests (e.g. cookies / user-agent).
        HttpHeaders headers = setHeaders(resource.getRequestHeaders(), cookieManager.getCookies(getHostFromURL(url)));

        // If referrer is null we must return the connection.
        if (referrer == null)  {
        	try {
        		return ConnectionFactory.getInstance().createConnection(url, headers);
        	} catch (Exception e) {}
        } else {
            // If referrer is provided we can set up the connection on a separate thread.
            SecondaryResourceFetchThread.enqueue(resource, referrer, headers);
        }

        return null;
    }

    /**
     * @see net.rim.device.api.browser.RenderingApplication#invokeRunnable(java.lang.Runnable)
     */
    public void invokeRunnable(Runnable runnable) {       
        (new Thread(runnable)).start();
    }    
    
    //-----------------------------------------------------------------------------
    //Organizes HTTP Headers into a single collection:
    //
    public HttpHeaders setHeaders(HttpHeaders existingHeaders, String sCookies) {
        HttpHeaders headers = new HttpHeaders();
        
        //Use headers provided by calling method  (e.g. "referrer"):
        if (existingHeaders != null) {
            headers.setProperties(existingHeaders);
        }

        // Headers to be included in all HTTP requets:
        headers.setProperty("User-Agent", ConnectionFactory.getUserAgent());
        headers.setProperty("Profile", ConnectionFactory.getProfile());
        headers.setProperty("x-wap-profile", ConnectionFactory.getProfile());
        headers.setProperty("x-rim-transcode-content", "*/*");
        headers.setProperty("Accept", "application/vnd.rim.html,text/html,application/xhtml+xml,application/vnd.wap.xhtml+xml,application/vnd.wap.wmlc;q=0.9,application/vnd.wap.wmlscriptc;q=0.7,text/vnd.wap.wml;q=0.7,text/vnd.sun.j2me.app-descriptor,image/vnd.rim.png,image/jpeg,application/x-vnd.rim.pme.b,application/vnd.rim.ucs,image/gif;anim=1,application/vnd.rim.css;v=1,text/css;media=screen,*/*;q=0.5");
        headers.setProperty("x-rim-original-accept", "application/vnd.rim.html,text/html,application/xhtml+xml,application/vnd.wap.xhtml+xml,application/vnd.wap.wmlc;q=0.9,application/vnd.wap.wmlscriptc;q=0.7,text/vnd.wap.wml;q=0.7,text/vnd.sun.j2me.app-descriptor,image/vnd.rim.png,image/jpeg,application/x-vnd.rim.pme.b,application/vnd.rim.ucs,image/gif;anim=1,application/vnd.rim.css;v=1,text/css;media=screen,*/*;q=0.");
        headers.setProperty("x-rim-gw-properties", "16.10");
        headers.setProperty("x-rim-accept-encoding", "yk;v=3;m=384");

        //Add Cookie header to HTTP Request if cookies exists
        if (!sCookies.equals("")) {
            headers.setProperty("Cookie", sCookies);
        }
        
        return headers;
    }
    
    private static String getHostFromURL(String url) {
        int end_protocol = url.indexOf("//");
        
        if (end_protocol < 0)
            return url;

        String rem_protocol = url.substring(end_protocol + 2, url.length());

        int end = rem_protocol.indexOf("/");
        String host = "";
        
        if (end >= 0) {
            host = rem_protocol.substring(0, end);
        } else {
            host = rem_protocol;
        }
        return host;    
    }

    /**
     * PrimaryResourceFetchThread
     * 
     * @author Eki Baskoro
     * @version 0.1
     *
     */
    private class PrimaryResourceFetchThread extends Thread 
    {
        
        private String _url;
        private HttpHeaders _requestHeaders;
        private byte[] _postData;
        private Event _event;
        
        PrimaryResourceFetchThread(String url, HttpHeaders requestHeaders, byte[] postData, Event event) {
            //Retrieves cookies saved for the domain being request, and set them as HTTP headers
            String sCookies = cookieManager.getCookies(getHostFromURL(url));
            
            _url = url;
            _requestHeaders = setHeaders(requestHeaders, sCookies);
            _postData = postData;
            _event = event;
        }
        
        public void run() {
            //at this point, all headers should be added to the HeadersCollection:
        	try {
        		HttpConnection connection = ConnectionFactory.getInstance().createConnection(_url, _requestHeaders, _postData);
                processConnection(connection, _event);
        	} catch (Exception e) {}
        }
        
    }
    
}

/**
 * SecondaryResourceFetchThread
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
class SecondaryResourceFetchThread extends Thread 
{

    /**
     * Callback browser field.
     */
    private BrowserContent browserField;
    
    /**
     * Connection headers.
     */
    private HttpHeaders headers;
    
    /**
     * Images to retrieve.
     */
    private Vector imageQueue;
    
    /**
     * True is all images have been enqueued.
     */
    private boolean done;
    
    /**
     * Sync object.
     */
    private static Object syncObject = new Object();
    
    /**
     * Singleton
     */
    private static SecondaryResourceFetchThread instance;
    
    
    /**
     * Enqueues secondary resource for a browser field.
     * 
     * @param resource - resource to retrieve.
     * @param referrer - call back browsr field.
     */
    public static void enqueue(RequestedResource resource, BrowserContent referrer, HttpHeaders requestHeaders) {
        if (resource == null) 
        	return;
        
        synchronized (syncObject) {
            // Create new thread.
            if (instance == null) {
                instance = new SecondaryResourceFetchThread();
                instance.start();
                instance.headers = requestHeaders;
            } else {
                // If thread alread is running, check that we are adding images for the same browser field.
                if (referrer != instance.browserField) {
                    synchronized (instance.imageQueue) {
                        // If the request is for a different browser field clear old elements.
                        instance.imageQueue.removeAllElements();
                    }
                }
            }   
            
            synchronized (instance.imageQueue) {
                instance.imageQueue.addElement(resource);
            }
            
            instance.browserField = referrer;
        }
    }
    
    /**
     * Default Constructor.
     *
     */
    private SecondaryResourceFetchThread() {
        imageQueue = new Vector();        
    }
    
    /**
     * Indicate that all images have been enqueued for this browser field.
     * 
     */
    static void doneAddingImages() {
        synchronized (syncObject) {
            if (instance != null) {
                instance.done = true;
            }
        }
    }
    
    public void run() {
        while (true) {
            if (done) {
                // Check if we are done requesting images.
                synchronized (syncObject) {
                    synchronized (imageQueue) {
                        if (imageQueue.size() == 0) {
                            instance = null;   
                            break;
                        }
                    }
                }
            }
            
            RequestedResource resource = null;
                              
            // Request next image.
            synchronized (imageQueue) {
                if (imageQueue.size() > 0) {
                    resource = (RequestedResource)imageQueue.elementAt(0);
                    imageQueue.removeElementAt(0);
                }
            }
            
            if (resource != null){
                //pass headers to secondary resource request:
                if (headers == null) {
                    headers = new HttpHeaders();
                }
                
                headers.setProperties(resource.getRequestHeaders());

                try {
                    HttpConnection connection = ConnectionFactory.getInstance().createConnection(resource.getUrl(), headers);
                    resource.setHttpConnection(connection);
                } catch (Exception e) {}
                
                // Signal to the browser field that resource is ready.
                if (browserField != null) {            
                    browserField.resourceReady(resource);
                }
            }
        }
    }
    
}