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

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.channels.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Trace
{
	/* Static Properties */
	public static String
	getFile()
	{
		return _file;
	}

	public static void
	setFile(String file)
	{
		_file = file;

		try
		{
			if (_handle != null && _handle != System.out)
				_handle.close();
			if (_stream != null)
				_stream.close();
		}
		catch (Exception e)
		{
		}

		if (_file == null || _file.equals(""))
		{
			_file = "";
			_stream = null;
			_handle = null;
		}
		else if (_file.equals("STDOUT"))
		{
			_stream = null;
			_handle = System.out;
		}
		else
		{
			try
			{
				_stream = new FileOutputStream(_file);
				_handle = new PrintStream(_stream);
			}
			catch (Exception e)
			{
				_file = "";
				_stream = null;
				_handle = null;
			}
		}
	}

	public static int
	getLevel()
	{
		return _level;
	}

	public static void
	setLevel(int level)
	{
		_level = level;
	}

	public static String
	getPrefix()
	{
		return _prefix;
	}

	public static void
	setPrefix(String prefix)
	{
		_prefix = prefix;
	}

	public static void
	write(int level, String format, Object... args)
	{
		writeArgs(level, format, args);
	}
	
	public static void
	write(int level, String format)
	{
		writeArgs(level, format, null);
	}
	
	static void
	writeArgs(int level, String format, Object[] args)
	{
		if (_handle == null)
			return;
		if (level > _level)
			return;

		/* time */
		GregorianCalendar cal = new GregorianCalendar();
		String y = String.format("%d", cal.get(Calendar.YEAR));
		String m = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		String d = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
		String D = y + "-" + m + "-" + d;

		String H = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
		String M = String.format("%02d", cal.get(Calendar.MINUTE));
		String S = String.format("%02d", cal.get(Calendar.SECOND));
		String T = H + ":" + M + ":" + S;

		/* process id */
		if (_pid == null)
		{
			_pid = ManagementFactory.getRuntimeMXBean().getName();
			_pid = _pid.replaceAll("@.*$", "");
		}

		/* function information */
		/* TODO */

		/* Build the prefix */
		String prefix = _prefix;
		prefix = prefix.replaceAll("%y", y);
		prefix = prefix.replaceAll("%m", m);
		prefix = prefix.replaceAll("%d", d);
		prefix = prefix.replaceAll("%D", D);

		prefix = prefix.replaceAll("%H", H);
		prefix = prefix.replaceAll("%M", M);
		prefix = prefix.replaceAll("%S", S);
		prefix = prefix.replaceAll("%T", T);

		prefix = prefix.replaceAll("%l", Integer.toString(level));

		prefix = prefix.replaceAll("%p", _pid);

		/* Write it out */
		FileLock lock = null;
		if (_stream != null)
		{
			FileChannel channel = _stream.getChannel();
			try
			{
				lock = channel.lock();
			}
			catch (Exception e)
			{
				return;
			}
			try
			{
				channel.position(channel.size());
			}
			catch (Exception e)
			{
				try
				{
					lock.release();
				}
				catch (Exception i)
				{
				}
				return;
			}
		}
		_handle.print(prefix);
		_handle.printf(format, args);
		_handle.println();
		_handle.flush();
		if (lock != null)
		{
			try
			{
				lock.release();
			}
			catch (Exception e)
			{
			}
		}
	}

	private static String _file = "STDOUT";
	private static FileOutputStream _stream = null;
	private static PrintStream _handle = System.out;

	private static int _level = 1;
	private static String _prefix = "%D %T: ";
	private static String _pid = null;
}
