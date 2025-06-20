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

@SuppressWarnings("serial")
public class IMuException extends Exception
{
	private final String _id;
	private Object[] _args;

	public IMuException(String id, Object... args)
	{
		super(buildMessage(id, args));
		_id = id;
		_args = args;
		Trace.write(2, "exception: %s", toString());
	}

	public IMuException(String id, Throwable cause)
	{
		super(buildMessage(id, cause != null ? cause.getMessage() : null), cause);
		_id = id;
		_args = (cause != null) ? new Object[] { cause.getMessage() } : null;
		Trace.write(2, "exception: %s", toString());
	}

	public Object[] getArgs() {
		return _args;
	}

	public void setArgs(Object[] args) {
		_args = args;
	}

	public String getID() {
		return _id;
	}

	@Override
	public String toString()
	{
		String str = _id;
		if (_args != null && _args.length > 0)
		{
			str += " (";
			for (int i = 0; i < _args.length; i++)
			{
				if (i > 0) str += ", ";
				str += String.valueOf(_args[i]);
			}
			str += ")";
		}
		return str;
	}

	private static String buildMessage(String id, Object... args)
	{
		StringBuilder sb = new StringBuilder(id);
		if (args != null && args.length > 0)
		{
			sb.append(" (");
			for (int i = 0; i < args.length; i++)
			{
				if (i > 0) sb.append(", ");
				sb.append(String.valueOf(args[i]));
			}
			sb.append(")");
		}
		return sb.toString();
	}
}