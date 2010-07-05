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
 * Post
 * 
 * Represents a Facebook post.
 * 
 * @author Eki Baskoro
 * @version 0.1
 * 
 */
public interface Post
{

	/**
	 * Obtain the user's ID.
	 * 
	 * @return the ID.
	 */
	public String getId();
	
	/**
	 * Obtain the post's type.
	 * 
	 * @return the type.
	 */
	public String getType();
	
	/**
	 * Obtain the post's attribution. This shows which application does the posting.
	 * 
	 * @return the attribution.
	 */
	public String getAttribution();
	
	/**
	 * Obtain the post's poster user.
	 * 
	 * @return an instance of user.
	 */
	public User getPoster();
	
	/**
	 * Obtain the post's message.
	 * 
	 * @return the message.
	 */
	public String getMessage();
	
	/**
	 * Obtain the URL of included picture.
	 * 
	 * @return the URL.
	 */
	public String getPictureUrl();
	
	/**
	 * Obtain the URL of included link.
	 * 
	 * @return the URL.
	 */
	public String getLinkUrl();
	
	/**
	 * Obtain the link's name.
	 * 
	 * @return the name.
	 */
	public String getLinkName();
	
	/**
	 * Obtain the link's caption.
	 * 
	 * @return the caption.
	 */
	public String getLinkCaption();
	
	/**
	 * Obtain the link's description.
	 * 
	 * @return the description.
	 */
	public String getLinkDescription();
	
	/**
	 * Obtain the included source's URL.
	 * 
	 * @return the URL.
	 */
	public String getSourceUrl();
	
	/**
	 * Obtain the icon's URL.
	 * 
	 * @return the URL.
	 */
	public String getIconUrl();
	
	/**
	 * Obtain the number of people who like the post.
	 * 
	 * @return the number of likes.
	 */
	public int getLikesCount();
	
	/**
	 * Obtain the comments for this post.
	 * 
	 * @return an array of Post instances.
	 */
	public Post[] getComments();
	
}
