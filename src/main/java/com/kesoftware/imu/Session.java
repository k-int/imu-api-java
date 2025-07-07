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

import java.net.*;

/*!
** Manages a connection to an IMu server.
**
** The server’s host name and port can be specified by setting properties on
** the object or by setting class-based default properties.
**
** @usage
**   com.kesoftware.imu.Session
** @end
**
** @since 1.0
*/
public class Session
{
	/* Static Properties */
	/*!
	** @property defaultHost
	**
	** The name of the host to connect to if no object-specific host has been
	** supplied.
	*/
	public static String
	getDefaultHost()
	{
		return _defaultHost;
	}

	public static void
	setDefaultHost(String host)
	{
		_defaultHost = host;
	}

	/*!
	** @property defaultPort
	**
	** The number of the port on the host if no object-specific port has been
	** supplied.
	*/
	public static int
	getDefaultPort()
	{
		return _defaultPort;
	}

	public static void
	setDefaultPort(int port)
	{
		_defaultPort = port;
	}
	
	/* Constructors */
	/*!
	** Creates a `Session` object with the specified ``host`` and ``port``.
	**
	** @param host
	**   The host to connect to.
	**
	** @param port
	**   The port on the host to connect on.
	*/
	public
	Session(String host, int port)
	{
		initialise();
		_host = host;
		_port = port;
	}

	/*!
	** Creates a `Session` object with the specified ``host``.
	**
	** The port to connect on will be taken from the
	** **defaultPort** [$<link>(:session:defaultPort)] class property.
	**
	** @param host
	**   The host to connect to.
	*/
	public
	Session(String host)
	{
		initialise();
		_host = host;
	}

	/*!
	** Creates a Session object with the specified port.
	**
	** The host to connect to will be taken from the
	** **defaultHost** [$<link>(:session:defaultHost)] class property.
	**
	** @param port
	**   The port on the host to connect on.
	*/
	public
	Session(int port)
	{
		initialise();
		_port = port;
	}
	
	/*!
	** Creates a Session object.
	**
	** The host to connect to will be taken from the
	** **defaultHost** [$<link>(:session:defaultHost)] class property. 
	**
	** The port to connect on will be taken from the
	** **defaultPort** [$<link>(:session:defaultPort)] class property.
	*/
	public
	Session()
	{
		initialise();
	}

	/* Properties */
	/*!
	** @property close
	**
	** A flag controlling whether the connection to the server should be closed
	** after the next request.
	** This flag is passed to the server as part of the next request to allow 
	** it to clean up.
	*/
	public boolean
	getClose()
	{
		if (_close == null)
			return false;
		return _close.booleanValue();
	}

	public void
	setClose(boolean close)
	{
		_close = new Boolean(close);
	}

	/*!
	** @property context
	**
	** The unique identifier assigned by the server to the current session.
	*/
	public String
	getContext()
	{
		return _context;
	}

	public void
	setContext(String context)
	{
		_context = context;
	}

	/*!
	** @property host
	**
	** The name of the host to connect to.
	** Setting this property after the connection has been established has no
	** effect.
	*/
	public String
	getHost()
	{
		return _host;
	}

	public void
	setHost(String host)
	{
		_host = host;
	}

	/*!
	** @property port
	**
	** The number of the port on the host to connect to.
	** Setting this property after the connection has been established has no
	** effect.
	*/
	public int
	getPort()
	{
		return _port;
	}

	public void
	setPort(int port)
	{
		_port = port;
	}

	/*!
	** @property suspend
	**
	** A flag controlling whether the server process handling this session
	** should begin listening on a distinct, process-specific port to ensure a
	** new session connects to the same server process.
	**
	** This is part of IMu's mechanism for maintaining state. 
	** If this flag is set to true, then after the next request is made to the 
	** server, the `Session`'s port property will be altered to the 
	** process specific port number.
	*/
	public boolean
	getSuspend()
	{
		if (_suspend == null)
			return false;
		return _suspend.booleanValue();
	}

	public void
	setSuspend(boolean suspend)
	{
		_suspend = new Boolean(suspend);
	}
	
	/* Methods */
	/*!
	** Open a connection to an IMu server.
	**
	** @throws IMuException
	**   The connection could not be opened.
	*/
	public void
	connect() throws IMuException
	{
		if (_socket != null)
			return;

		Trace.write(2, "connecting to %s:%d", _host, _port);
		try
		{
			_socket = new Socket(_host, _port);
		}
		catch (Exception e)
		{
			String mesg = e.getMessage();
			Trace.write(2, "connection failed: %s", mesg);
			throw new IMuException("SessionConnect", _host, _port, mesg);
		}
		Trace.write(2, "connected ok");
		
		//logging_socket = new LoggingSocket(_socket);
        
		_stream = new Stream(_socket);
	}

	/*!
	** Closes the connection to the IMu server.
	*/
	public void
	disconnect()
	{
		if (_socket == null)
			return;

		Trace.write(2, "closing connection");
		try
		{
			_socket.close();
		}
		catch (Exception e)
		{
			// ignore
		}
		initialise();
	}

	/*!
	** Logs in as the given user with the given password.
	**
	** The ``password`` parameter may be ``null``. 
	** This will cause the server to use server-side authentication (such as 
	** .rhosts authentication) to authenticate the user.
	** 
	** @param login
	**   The name of the user to login as.
	**
	** @param password
	**   The user's password for authentication.
	**
	** @param spawn
	**   A flag indicating whether the process should create a new child
	**   process specifically for handling the newly logged in user's requests. 
	**
	** @throws IMuException
	**   The login request failed.
	**
	** @throws Exception
	**   A low-level socket error occurred.
	*/
	public Map
	login(String login, String password, boolean spawn) throws Exception
	{
		Map request = new Map();
		request.put("login", login);
		request.put("password", password);
		request.put("spawn", spawn);
		return request(request);
	}

	/*!
	** Logs in as the given user with the given password.
	**
	** The ``password`` parameter may be ``null``. 
	** This will cause the server to use server-side authentication (such as 
	** .rhosts authentication) to authenticate the user.
	**
	** ``Spawn`` defaults to ``true`` causing the server to create
	** a new child process specifically for handling the newly logged in
	** user's requests.
	**
	** @param login
	**   The name of the user to login as.
	**
	** @param password
	**   The user's password for authentication.
	**
	** @throws IMuException
	**   The login request failed.
	**
	** @throws Exception
	**   A low-level socket error occurred.
	*/
	public Map
	login(String login, String password) throws Exception
	{
		return login(login, password, true);
	}

	/*!
	** Logs in as the given user.
	**
	** Uses server-side authentication (such as .rhosts authentication) to
	** authenticate the user.
	**
	** The server will spawn a new child process specifically for handling
	** the newly logged in user's requests.
	**
	** @param login
	**   The name of the user to login as.
	**
	** @throws IMuException
	**   The login request failed.
	**
	** @throws Exception
	**   A low-level socket error occurred.
	*/
	public Map
	login(String login) throws Exception
	{
		return login(login, null, true);
	}

	/*!
	** Logs the user out of the server.
	**
	** @since 2.0
	*/
	public Map
	logout() throws Exception
	{
		Map request = new Map();
		request.put("logout", true);
		return request(request);
	}

	/*!
	** Submits a low-level request to the IMu server.
	**
	** @param request
	**   A `Map` object [$<link>(:map:map)] containing the request parameters.
	**
	** @returns
	**   A `Map` object [$<link>(:map:map)] containing the server's response.
	**
	** @throws IMuException
	**   A server-side error occurred.
	*/
	public Map
	request(Map request) throws IMuException
	{
		connect();

		if (_close != null)
			request.put("close", _close);
		if (_context != null)
			request.put("context", _context);
		if (_suspend != null)
			request.put("suspend", _suspend);

		_stream.put(request);
		Object raw = _stream.get();
		
		
		//System.out.println( "=== INPUT ===" );
		//System.out.println( logging_socket.getInputLog() );
		//System.out.println( "=== OUTPUT ===" );
		//System.out.println( logging_socket.getOutputLog() );
		
		//logging_socket.clearLogs();
		
		if (! (raw instanceof Map))
		{
			String type = raw.getClass().getName();
			Trace.write(2, "bad type of response from server: %s", type);
			throw new IMuException("SessionResponse", type);
		}
		Map response = (Map) raw;

		if (response.containsKey("context"))
			_context = response.getString("context");
		if (response.containsKey("reconnect"))
			_port = response.getInt("reconnect");

		boolean disconnect = false;
		if (_close != null)
			disconnect = _close.booleanValue();
		if (disconnect)
			disconnect();

		String status = response.getString("status");
		if (status.equals("error"))
		{
			Trace.write(2, "server error");

			String id = "SessionServerError";
			if (response.containsKey("error"))
				id = response.getString("error");
			else if (response.containsKey("id"))
				id = response.getString("id");

			IMuException e = new IMuException(id);

			if (response.containsKey("args"))
				e.setArgs(response.getArray("args", Object[].class));

			Trace.write(2, "throwing exception %s", e.toString());

			throw e;
		}

		return response;
	}
	
	private static String _defaultHost = "127.0.0.1";
	private static int _defaultPort = 40000;

	private Boolean _close;
	private String _context;
	private String _host;
	private int _port;
	private Socket _socket;
	//private LoggingSocket logging_socket;
	private Stream _stream;
	private Boolean _suspend;

	private void
	initialise()
	{
		_close = null;
		_context = null;
		_host = _defaultHost;
		_port = _defaultPort;
		_socket = null;
		_stream = null;
		_suspend = null;
	}
}
