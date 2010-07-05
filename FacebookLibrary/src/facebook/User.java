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
 * User
 * 
 * Represents a Facebook user.
 * 
 * @author Eki Baskoro
 * @version 0.1
 * 
 */
public interface User
{

	/**
	 * Obtain the user's ID.
	 * 
	 * @return the ID.
	 */
	public String getId();
	
	/**
	 * Obtain the user's first name.
	 * 
	 * @return the first name.
	 */
	public String getFirstName();
	
	/**
	 * Obtain the user's last name.
	 * 
	 * @return the last name.
	 */
	public String getLastName();
	
	/**
	 * Obtain the user's full name.
	 * 
	 * @return the full name.
	 */
	public String getFullName();
	
	/**
	 * Obtain the user's profile URL.
	 * 
	 * @return the URL.
	 */
	public String getProfileUrl();
	
	/**
	 * Obtain the user's about information.
	 * 
	 * @return the about information.
	 */
	public String getAboutText();
	
	/**
	 * Obtain the user's birthday.
	 * 
	 * @return the birthday.
	 */
	public String getBirthday();
	
	/**
	 * Obtain the user's website.
	 * 
	 * @return the website URL.
	 */
	public String getWebsite();
	
	/**
	 * Obtain the user's home town.
	 * 
	 * @return the home town.
	 */
	public String getHomeTown();
	
	/**
	 * Obtain the user's location.
	 * 
	 * @return the location.
	 */
	public String getLocation();
	
	/**
	 * Obtain the user's gender.
	 * 
	 * @return the gender.
	 */
	public String getGender();
	
	/**
	 * Obtain the user's friends.
	 * 
	 * @return an array of user instances.
	 */
	public User[] getFriends();
	
	/**
	 * Obtain the user's news feed.
	 * 
	 * @return an array of Post instances.
	 */
	public Post[] getStream();
	
	/**
	 * Obtain the user's news feed limit by limit amount of posts.
	 * 
	 * @param limit the number of posts to return.
	 * @return an array of Post instances.
	 */
	public Post[] getStream(int limit);
	
	/**
	 * Publish a post to user's wall.
	 * 
	 * @param message the message.
	 * @param linkUrl the link's URL.
	 * @param linkName the link's name.
	 * @param linkCaption the link's caption.
	 * @param linkDescription the link's description.
	 */
	public void publishStream(String message, String linkUrl, String linkName, String linkCaption, String linkDescription);
	
	/**
	 * Publish a post to user's wall.
	 * 
	 * @param message the message.
	 * @param pictureUrl the picture's URL.
	 * @param linkUrl the link's URL.
	 * @param linkName the link's name.
	 * @param linkCaption the link's caption.
	 * @param linkDescription the link's description.
	 * @param sourceUrl the source URL.
	 */
	public void publishStream(String message, String pictureUrl, String linkUrl, String linkName, String linkCaption, String linkDescription, String sourceUrl);
	
	/**
	 * Change the user's status.
	 * 
	 * @param status the new status message.
	 */
	public void setStatus(String status);
	
}
