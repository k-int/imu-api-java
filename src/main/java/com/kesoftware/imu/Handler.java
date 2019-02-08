/* KE Software Open Source Licence
** 
** Notice: Copyright (c) 2011-2013 KE SOFTWARE PTY LTD (ACN 006 213 298)
** (the "Owner"). All rights reserved.
** 
** Licence: Permission is hereby granted, free of charge, to any person
** obtaining a copy of this software and associated documentation files
** (the "Software"), to deal with the Software without restriction,
** including without limitation the rights to use, copy, modify, merge,
** publish, distribute, sublicense, and/or sell copies of the Software,
** and to permit persons to whom the Software is furnished to do so,
** subject to the following conditions.
** 
** Conditions: The Software is licensed on condition that:
** 
** (1) Redistributions of source code must retain the above Notice,
**     these Conditions and the following Limitations.
** 
** (2) Redistributions in binary form must reproduce the above Notice,
**     these Conditions and the following Limitations in the
**     documentation and/or other materials provided with the distribution.
** 
** (3) Neither the names of the Owner, nor the names of its contributors
**     may be used to endorse or promote products derived from this
**     Software without specific prior written permission.
** 
** Limitations: Any person exercising any of the permissions in the
** relevant licence will be taken to have accepted the following as
** legally binding terms severally with the Owner and any other
** copyright owners (collectively "Participants"):
** 
** TO THE EXTENT PERMITTED BY LAW, THE SOFTWARE IS PROVIDED "AS IS",
** WITHOUT ANY REPRESENTATION, WARRANTY OR CONDITION OF ANY KIND, EXPRESS
** OR IMPLIED, INCLUDING (WITHOUT LIMITATION) AS TO MERCHANTABILITY,
** FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. TO THE EXTENT
** PERMITTED BY LAW, IN NO EVENT SHALL ANY PARTICIPANT BE LIABLE FOR ANY
** CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
** TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
** SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
** 
** WHERE BY LAW A LIABILITY (ON ANY BASIS) OF ANY PARTICIPANT IN RELATION
** TO THE SOFTWARE CANNOT BE EXCLUDED, THEN TO THE EXTENT PERMITTED BY
** LAW THAT LIABILITY IS LIMITED AT THE OPTION OF THE PARTICIPANT TO THE
** REPLACEMENT, REPAIR OR RESUPPLY OF THE RELEVANT GOODS OR SERVICES
** (INCLUDING BUT NOT LIMITED TO SOFTWARE) OR THE PAYMENT OF THE COST OF SAME.
*/
package com.kesoftware.imu;

/*!
** Provides a general low-level interface to creating server-side objects.
**
** @usage
**   com.kesoftware.imu.Handler
** @end
**
** @since 1.0
*/
public class Handler
{
	/* Constructor */
	/*!
	** Provides a general low-level interface to creating server-side objects.
	**
	** @param session
	**   A `Session` [$<link>(:session:session)] object to be used to
	**   communicate with the IMu server.
	*/
	public
	Handler(Session session)
	{
		_session = session;

		_create = null;
		_destroy = null;
		_id = null;
		_language = null;
		_name = null;
	}
	
	/*!
	** Creates an object which can be used to interact with server-side objects.
	** A new session is created automatically using the `Session` 
	** [$<link>(:session:session)] class's default host and port values.
	*/
	public Handler()
	{
		this(new Session());
	}

	/* Properties */
	/*!
	** @property create
	**   An object to be passed to the server when the server-side object is 
	**   created.
	**
	**   To have any effect this must be set before any object methods 
	**   are called. This property is usually only set by sub-classes of 
	**   `Handler` [$<link>(:handler:handler)].
	*/
	public Object
	getCreate()
	{
		return _create;
	}

	public void
	setCreate(Object create)
	{
		_create = create;
	}

	/*!
	** @property destroy
	**   A flag controlling whether the corresponding server-side object should 
	**   be destroyed when the session is terminated.
	*/
	public boolean
	getDestroy()
	{
		if (_destroy == null)
			return false;
		return _destroy.booleanValue();
	}

	public void
	setDestroy(boolean destroy)
	{
		_destroy = new Boolean(destroy);
	}

	/*!
	** @property id
	**   The unique identifier assigned to the server-side object once it has 
	**   been created.
	*/
	public String
	getID()
	{
		return _id;
	}

	public void
	setID(String id)
	{
		_id = id;
	}

	/*!
	** @property language
	**   The language to be used in the server.
	*/
	public String
	getLanguage()
	{
		return _language;
	}

	public void
	setLanguage(String language)
	{
		_language = language;
	}

	/*!
	** @property name
	**   The name of the server-side object to be created.
	**   This must be set before any object methods are called.
	*/
	public String
	getName()
	{
		return _name;
	}

	public void
	setName(String name)
	{
		_name = name;
	}

	/*!
	** @property session
	**   The `Session` object [$<link>(:session:session)] used by the handler
	**   to communicate with the IMu server.
	*/
	public Session
	getSession()
	{
		return _session;
	}

	/* Methods */
	/*!
	** Calls a method on the server-side object.
	**
	** @param method
	**   The name of the method to be called.
	**
	** @param parameters
	**   Any parameters to be passed to the method.
	**   The **call( )** method uses Java's reflection to determine the
	**   structure of the parameters to be transmitted to the server.
	**
	** @returns 
	**   An `Object` containing the result returned by the server-side method.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	**/
	public Object
	call(String method, Object parameters) throws IMuException
	{
		Map request = new Map();
		request.put("method", method);
		if (parameters != null)
			request.put("params", parameters);
		Map response = request(request);
		return response.get("result");
	}

	/*!
	** Calls a method on the server-side object.
	**
	** @param method
	**   The name of the method to be called.
	**
	** @returns
	**   An ``Object`` containing the result returned by the the server-side method.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public Object
	call(String method) throws IMuException
	{
		return call(method, null);
	}

	/*!
	** Submits a low-level request to the IMu server. 
	** This method is chiefly used by the **call( )** method above.
	**
	** @param request
	**   A `Map` object [$<link>(:map:map)] containing the request parameters.
	**
	** @returns
	**   A `Map` object [$<link>(:map:map)] containing the server's response.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public Map
	request(Map request) throws IMuException
	{
		if (_id != null)
			request.put("id", _id);
		else if (_name != null)
		{
			request.put("name", _name);
			if (_create != null)
				request.put("create", _create);
		}
		if (_destroy != null)
			request.put("destroy", _destroy.booleanValue());
		if (_language != null)
			request.put("language", _language);

		Map response = _session.request(request);

		if (response.containsKey("id"))
			_id = response.getString("id");

		return response;
	}

	protected Session _session;

	protected Object _create;
	protected Boolean _destroy;
	protected String _id;
	protected String _language;
	protected String _name;
}
