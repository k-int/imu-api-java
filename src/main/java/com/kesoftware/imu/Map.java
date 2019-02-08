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

import java.util.Arrays;
import java.util.HashMap;

@SuppressWarnings("serial")

/*!
** Provides a simple map class with string keys and a set of convenience
** methods for getting values of certain types.
**
** @extends java.util.Arrays.HashMap<String, Object>
**
** @usage
**   com.kesoftware.imu.Map
** @end
**
** @since 1.0
*/
public class Map extends HashMap<String,Object>
{
	/* Properties */
	/*!
	** Gets the value associated with the key ``name`` and returns it as an 
	** array of the type specified by ``type``.
	**
	** @param name
	**   The key whose associated value is to be returned.
	**
	** @param type
	**   The type of the array required.
	**
	** @returns mixed
	**   The correctly typed array.
	**
	** @usage
	**   long[] array = map.getArray("key", Long[].class);
	** @end
	*/
	public <T> T[]
	getArray(String name, Class<T[]> type)
	{
		Object value = get(name);
		if (value == null)
			return null;
		Object[] array = (Object []) value;
		return Arrays.copyOf(array, array.length, type);
	}

	/*!
	** Gets the value associated with the key ``name`` and returns it as a 
	** `boolean`.
	**
	** @param name
	**   The key whose associated value is to be returned.
	**
	** @returns
	**   The value, interpreted as a `boolean`.
	**   ``Null`` values are considered ``false``.
	**   Numeric values are considered ``false`` if they evaluate to ``0`` 
	**   and ``true`` otherwise.
	**   Any other non-boolean value is converted to a `String` and then
	**   parsed as a `boolean`.
	*/
	public boolean
	getBoolean(String name)
	{
		Object value = get(name);
		if (value == null)
			return false;
		if (value instanceof Boolean)
			return (Boolean) value;
		
		if (value instanceof Integer)
			return ((Integer) value) != 0;
		if (value instanceof Long)
			return ((Long) value) != 0;
		if (value instanceof Double)
			return ((Double) value) != 0;
		
		return Boolean.valueOf(value.toString());
	}
	
	/*!
	** Gets the value associated with the key ``name`` and returns it as a
	** double precision floating point number.
	**
	** @param name
	**   The key whose associated value is to be returned.
	**
	** @returns
	**   The value, interpreted as a `double`.
	**   ``Null`` values evaluate to ``0``. 
	**   `Boolean` values evaluate to ``0`` if ``false`` and 1 if ``true``. 
	**   Any other non-numeric value is converted to a `String` and then
	**   parsed as a `Double`.
	*/
	public double
	getDouble(String name)
	{
		Object value = get(name);
		if (value == null)
			return 0.0;
		if (value instanceof Double)
			return (Double) value;
		
		if (value instanceof Boolean)
			return ((Boolean) value).booleanValue() ? 1.0 : 0.0;
		if (value instanceof Integer)
			return ((Integer) value).doubleValue();
		if (value instanceof Long)
			return ((Long) value).doubleValue();
		
		return Double.valueOf(value.toString());
	}
	
	/*!
	** Gets the value associated with the key ``name`` and returns it as an 
	** `int`.
	**
	** @param name
	**   The key whose associated value is to be returned.
	**
	** @returns
	**   The value, interpreted as an `int`.
	**   ``Null`` values evaluate to ``0``.
	**   `Boolean` values evaluate to ``0`` if ``false`` and ``1`` if ``true``. 
	**   Any other non-numeric value is converted to a `String` and then 
	**   parsed as an `int`.
	*/
	public int
	getInt(String name)
	{
		Object value = get(name);
		if (value == null)
			return 0;
		if (value instanceof Integer)
			return (Integer) value;
		
		if (value instanceof Boolean)
			return ((Boolean) value).booleanValue() ? 1 : 0;
		if (value instanceof Long)
			return ((Long) value).intValue();
		if (value instanceof Double)
			return ((Double) value).intValue();
		
		return Integer.valueOf(value.toString());
	}
	
	/*!
	** Gets the value associated with the key ``name`` and returns it as a 
	** `long`.
	**
	** @param name
	**   The key whose associated value is to be returned.
	**
	** @returns
	**   The value, interpreted as a `long`.
	**   ``Null`` values evaluate to ``0``.
	**   `Boolean` values evaluate to ``0`` if ``false`` and ``1`` if ``true``.
	**   Any other non-numeric value is converted to a `String` and then
	**   parsed as a `long`.
	*/
	public long
	getLong(String name)
	{
		Object value = get(name);
		if (value == null)
			return 0;
		if (value instanceof Long)
			return (Long) value;
		
		if (value instanceof Boolean)
			return ((Boolean) value).booleanValue() ? 1 : 0;
		if (value instanceof Integer)
			return ((Integer) value).longValue();
		if (value instanceof Double)
			return ((Double) value).longValue();
		
		return Long.valueOf(value.toString());
	}
	
	/*!
	** Gets the value associated with the key ``name`` and returns it as an IMu
	** `Map` object [$<link>(:map:map)].
	**
	** @param name
	**   The key whose associated value is to be returned.
	**
	** @returns
	**   The value, cast to a `Map` [$<link>(:map:map)].
	*/
	public Map
	getMap(String name)
	{
		return (Map) get(name);
	}
	
	/*!
	** Gets the value associated with the key ``name`` and returns it as an 
	** array of IMu `Map` objects [$<link>(:map:map)].
	** This is a short-hand for:
	** @code
	**   getArray(name, Map[].class)
	** @end  
	**
	** @param name
	**   The key whose associated value is to be returned.
	**
	** @returns
	**   The value, converted to a `Map[]`.
	*/
	public Map[]
	getMaps(String name)
	{
		return getArray(name, Map[].class);
	}
	
	/*!
	** Gets the value associated with the key ``name`` and returns it as a 
	** `String`.
	**
	** @param name
	**   The key whose associated value is to be returned.
	**
	** @returns
	**   The value, interpreted as a `String`.
	**   ``Null`` values remail ``null``.
	**   Any other non-string value is converted to a `String` using the 
	**   object's **toString( )** method.
	*/
	public String
	getString(String name)
	{
		Object value = get(name);
		if (value == null)
			return null;
		if (value instanceof String)
			return (String) value;
		
		return value.toString();
	}

	/*!
	** Gets the value associated with the key ``name`` and returns it as an 
	** array of `String`\s. This is a short-hand for:
	** @code
	**   getArray(name, String[].class)
	** @end
	**
	** @param name
	**   The key whose associated value is to be returned.
	**
	** @returns
	**   The value, converted to a `String[]`.
	*/
	public String[]
	getStrings(String name)
	{
		return getArray(name, String[].class);
	}
}
