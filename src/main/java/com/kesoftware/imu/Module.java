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

import java.util.ArrayList;

/*!
** Provides access to an EMu modlue.
**
** @extends com.kesoftware.imu.Handler
**
** @usage
**   com.kesoftware.imu.Module
** @end
**
** @since 1.0
*/
public class Module extends Handler
{
	/* Constructors */
	/*!
	** Creates an object which can be used to access the EMu module specified
	** by ``table``.
	**
	** @param table
	**   Name of the EMu module to be accessed.
	**
	** @param session
	**   A `Session` object [$<link>(:session:session)] to be used to
	**   communicate with the IMu server.
	*/
	public Module(String table, Session session)
	{
		super(session);
		initialise(table);
	}

	/*!
	** Creates an object which can be used to access the EMu module specified
	** by ``table`` and the `Session` [$<link>(:session:session)] class's
	** default **host** [$<link>(:session:host)] and **port**
	** [$<link>(:session:)] values.
	**
	** @param table
	**   Name of the EMu module to be accessed.
	*/
	public Module(String table)
	{
		super();
		initialise(table);
	}

	/* Properties */
	/*
	** @property table
	**   The name of the table associated with the `Module` object
	**   [$<link>(:module:module)].
	*/
	public String
	getTable()
	{
		return _table;
	}
	
	/* Methods */
	/*!
	** Associates a set of columns with a logical name in the server. 
	** The name can be used instead of a column list when retrieving data 
	** using **fetch( )** [$<link>(:module:fetch)].
	**
	** @param name
	**   The logical name to associate with the set of columns.
	**
	** @param columns
	**   A `String` containing the names of the columns to be used when ``name``
	**   is passed to **fetch( )** [$<link>(:module:fetch)]. 
	**   The column names must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @returns
	**   The number of sets (including this one) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public int
	addFetchSet(String name, String columns) throws IMuException
	{
		return doAddFetchSet(name, columns);
	}
	
	/*!
	** Associates a set of columns with a logical name in the server.
	** The name can be used instead of a column list when retrieving data
	** using **fetch( )** [$<link>(:module:fetch)].
	**
	** @param name
	**   The logical name to associate with the set of columns.
	**
	** @param columns
	**   An array of `String`\s containing the names of the columns to be used
	**   when ``name`` is passed to **fetch( )** [$<link>(:module:fetch)].
	**
	** @returns
	**   The number of sets (including this one) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public int
	addFetchSet(String name, String[] columns) throws IMuException
	{
		return doAddFetchSet(name, columns);
	}
	
	/*!
	** Associates a set of columns with a logical name in the server.
	** The name can be used instead of a column list when retrieving data
	** using **fetch( )** [$<link>(:module:fetch)].
	**
	** @param name
	**   The logical name to associate with the set of columns.
	**
	** @param columns
	**   A list of `String`\s containing the names of the columns to be used
	**   when ``name`` is passed to **fetch( )** [$<link>(:module:fetch)].
	**
	** @returns
	**   The number of sets (including this one) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public int
	addFetchSet(String name, ArrayList<String> columns) throws IMuException
	{
		return doAddFetchSet(name, columns.toArray());
	}
	
	/*!
	** Accociates several sets of columns with logical names in the server.
	** This is the equivalent of calling **addFetchSet( )**
	** [$<link>(:module:addFetchSet)] for each entry in the map but is more
	** efficient.
	**
	** @param sets
	**   A `Map` [$<link>(:map:map)] containing a set of mappings between a
	**   name and a set of columns.
	** 
	** @returns
	**   The number of sets (including these ones) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public int
	addFetchSets(Map sets) throws IMuException
	{
		Long count = (Long) call("addFetchSets", sets);
		return count.intValue();
	}
	
	/*!
	** Associates a set of columns with a logical name in the server. 
	** The name can be used when specifying search terms to be passed to 
	** **findTerms( )** [$<link>(:module:findTerms)].
	** The search becomes the equivalent of an ``OR`` search involving the 
	** columns.
	**
	** @param name 
	**   The logical name to associate with the set of columns.
	**
	** @param columns 
	**   A `String` containing the names of the columns to be used when ``name``
	**   is passed to **findTerms( )** [$<link>(:module:findTerms)].
	**   The column names must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @returns 
	**   The number of aliases (including this one) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public int
	addSearchAlias(String name, String columns) throws IMuException
	{
		return doAddSearchAlias(name, columns);
	}
	
	/*!
	**
    ** Associates a set of columns with a logical name in the server. 
    ** The name can be used when specifying search terms to be passed to 
    ** **findTerms( )** [$<link>(:module:findTerms)].
    ** The search becomes the equivalent of an ``OR`` search involving the 
    ** columns.
    **
    ** @param name 
    **   The logical name to associate with the set of columns.
    **
    ** @param columns 
	**   An array of `String`\s containing the names of columns to be used when
	**   ``name`` is passed to **findTerms( )** [$<link>(:module:findTerms)].
    **
    ** @returns 
    **   The number of aliases (including this one) registered in the server.
    **
    ** @throws IMuException
    **   If a server-side error occurred.
	*/
	public int
	addSearchAlias(String name, String[] columns) throws IMuException
	{
		return doAddSearchAlias(name, columns);
	}
	
	/*!
	** Associates a set of columns with a logical name in the server.
	** 
	** The name can be used when specifying search terms to be passed to
	** **findTerms( )** [$<link>(:module:findTerms)].
	** The search becomes the equivalent of an ``OR`` search involving the 
	** columns.
	**
	** @param name
	**   The logical name to be associated with the set of columns.
	**
	** @param columns
	**   An array of `String`\s containing the names of columns to be used when
	**   ``name`` is passed to **findTerms( )** [$<link>(:module:findTerms)].
	**
	** @returns
	**   The number of aliases (including this one) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public int
	addSearchAlias(String name, ArrayList<String> columns) throws IMuException
	{
		return doAddSearchAlias(name, columns.toArray());
	}
	
	/*!
	** Associates several sets of columns with logical names of the server. 
	**
	** This is the equivalent of calling **addSearchAlias( )**
	** [$<link>(:module:addSearchAlias)] for each entry in the map but
	** is more efficient.
	**
	** @param aliases 
	**   A `Map` [$<link>(:map:map)] containing a set of mappings between a
	**   name and a set of columns.
	**
	** @returns
	**   The number of sets (including these ones) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public int
	addSearchAliases(Map aliases) throws IMuException
	{
		Long count = (Long) call("addSearchAliases", aliases);
		return count.intValue();
	}
	
	/*!
	** Associates a set of sort keys with a logical name in the server. 
	**
	** The name can be used instead of a sort key list when sorting the current 
	** result set using **sort( )** [$<link>(:module:sort)].
	**
	** @param name
	**   The logical name to associate with the set of columns.
	**
	** @param keys 
	**   A `String` containing the names of the keys to be used when ``name`` 
	**   is passed to **sort( )** [$<link>(:module:sort)].
	**   The keys must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @returns 
	**   The number of sets (including this one) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public int
	addSortSet(String name, String keys) throws IMuException
	{
		return doAddSortSet(name, keys);
	}
	
	/*!
	** Associates a set of sort keys with a logical name in the server.
	**
	** @param name
	**   The logical name to associates with the set of columns.
	**
	** @param keys
	**   An array of `String`\s containing the names of the keys to be used
	**   when ``name`` is passed to **sort( )** [$<link>(:module:sort)].
	**   The keys must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @returns
	**   The number of sets (including this one) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public int
	addSortSet(String name, String[] keys) throws IMuException
	{
		return doAddSortSet(name, keys);
	}
	
	/*!
	** Associates a set of sort keys with a logical name in the server.
	**
	** @param name
	**   The logical name to associate with the set of columns.
	**
	** @param keys
	**   A list of `String`\s containing the names of the keys to be used
	**   when ``name`` is passed to **sort( )** [$<link>(:module:sort)].
	**   The keys must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @returns
	**   The number of sets (including this one) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public int
	addSortSet(String name, ArrayList<String> keys) throws IMuException
	{
		return doAddSortSet(name, keys.toArray());
	}
	
	/*!
	** Associates several sets of sort keys with logical names in the server.
	**
	** This is the equivalent of calling **addSortSet( )**
	** [$<link>(:module:addSortSet)] for each entry in the map but is more
	** efficient.
	**
	** @param sets
	**   A `Map` [$<link>(:map:map)] containing a set of mappings between a
	**   name and a set of keys.
	**
	** @returns
	**   The number of sets (including these ones) registered in the server.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public Object
	addSortSets(Map sets) throws IMuException
	{
		Long count = (Long) call("addSortSets", sets);
		return count.intValue();
	}

	/*!
	** Fetches ``count` records from the position described by a combination of
	** ``flag`` and ``offset``.
	**
	** @param flag
	**   The position to start fetching records from. Must be one of:
	**     ``start``
	**     ``current``
	**     ``end``
	**
	** @param offset
	**   The position relative to ``flag`` to start fetching from.
	**
	** @param count
	**   The number of recoreds to fetch.
	**   A ``count`` of ``0`` is permitted to change the location of the current
	**   record without returning any results.
	**   A ``count`` of less than ``0`` causes all the remaining records in the 
	**   result set to be returned.
	**
	** @param columns
	**   A `String` containing the names of the columns to be returned for each 
	**   record or the name of a column set which has been registered previously
	**   using **addFetchSet( )** [$<link>(:module:addFetchSet)]. 
	**   The column names must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @returns
	**   A `ModuleFetchResult` object.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public ModuleFetchResult
	fetch(String flag, long offset, int count, String columns)
		throws IMuException
	{
		return doFetch(flag, offset, count, columns);
	}

	/*!
	** Fetches ``count`` records from the position described by a combination
	** of ``flag`` and ``offset``.
	**
	** @param flag
	**   The position to start fetching records from.
	**   Must be one of:
	**     ``start``
	**     ``current``
	**     ``end``
	**
	** @param offset
	**   The position relative to ``flag`` to start fetching from.
	**
	** @param count
	**   The number of records to fetch.
	**   A ``count`` of ``0`` is permitted to change the location of the current
	**   record without returning any results.
	**   A ``count`` of less than ``0`` causes all the remaining records in the
	**   result set to be returned.
	**
	**   @param columns
	**     An array of `String`\s containing the names of the columns to be
	**     returned for each record or the name of a column set which has been
	**     registered previously using **addFetchSet( )**
	**     [$<link>(:module:addFetchSet)].
	**
	**   @returns
	**     A `ModuleFetchResult` object.
	**
	**   @throws IMuException
	**     If a server-side error occurred.
	*/
	public ModuleFetchResult
	fetch(String flag, int offset, int count, String[] columns)
		throws IMuException
	{
		return doFetch(flag, offset, count, columns);
	}

	/*!
	** Fetches ``count`` records from the position described by a combination
	** of ``flag`` and ``offset``.
	**
	** @param flag
	**   The position to start fetching records from.
	**   Must be one of:
	**     ``start``
	**     ``current``
	**     ``end``
	**
	** @param offset
	**   The position relative to ``flag`` to start fetching from.
	**
	** @param count
	**   The number of records to fetch.
	**   A ``count`` of ``0`` is permitted to change the location of the current
	**   record without returning any results.
	**   A ``count`` of less than ``0`` causes all the remaining records in the
	**   result set to be returned.
	**
	**   @param columns
	**     A list of `String`\s containing the names of the columns to be 
	**     returned for each record or the name of a column set which has been 
	**     registered previously using **addFetchSet( )**
	**     [$<link>(:module:addFetchSet)].
	**
	**   @returns
	**     A `ModuleFetchResult` object.
	**
	**   @throws IMuException
	**     If a server-side error occurred.
	*/
	public ModuleFetchResult
	fetch(String flag, int offset, int count, ArrayList<String> columns)
		throws IMuException
	{
		return doFetch(flag, offset, count, columns.toArray());
	}

	/*!
	** Fetches ``count`` records from the position described by a combination
	** of ``flag`` and ``offset``.
	**
	** @param flag
	**   The position to start fetching records from.
	**   Must be one of:
	**     ``start``
	**     ``current``
	**     ``end``
	**
	** @param offset
	**   The position relative to ``flag`` to start fetching from.
	**
	** @param count
	**   The number of records to fetch.
	**   A ``count`` of ``0`` is permitted to change the location of the current
	**   record without returning any results.
	**   A ``count`` of less than ``0`` causes all the remaining records in the
	**   result set to be returned.
	**
	**   @returns
	**     A `ModuleFetchResult` object.
	**
	**   @throws IMuException
	**     If a server-side error occurred.
	*/
	public ModuleFetchResult
	fetch(String flag, long offset, int count)
		throws IMuException
	{
		return doFetch(flag, offset, count, null);
	}

	public ModuleFetchResult
	fetch(ModuleFetchPosition pos, int count, String columns)
		throws IMuException
	{
		return doFetch(pos._flag, pos._offset, count, columns);
	}

	public ModuleFetchResult
	fetch(ModuleFetchPosition pos, int count, String[] columns)
		throws IMuException
	{
		return doFetch(pos._flag, pos._offset, count, columns);
	}

	public ModuleFetchResult
	fetch(ModuleFetchPosition pos, int count, ArrayList<String> columns)
		throws IMuException
	{
		return doFetch(pos._flag, pos._offset, count, columns.toArray());
	}

	public ModuleFetchResult
	fetch(ModuleFetchPosition pos, int count)
		throws IMuException
	{
		return doFetch(pos._flag, pos._offset, count, null);
	}

	/*!
	** Serches for a record with the key value ``key``.
	**
	** @param key
	**   The key of the record being searched for.
	**
	** @returns
	**   The number of records found. 
	**   This will be either ``1`` if the record was found or ``0`` if not found.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public long
	findKey(long key) throws IMuException
	{
		if(call("findKey", key) instanceof Number count)
			return count.longValue();
		else
			throw new IMuException(call("findKey", key) + " cannot be converted to a Number");
	}

	/*!
	** Searches for records with key values in the array ``keys``.
	**
	** @param keys
	**   The list of keys being searched for.
	**
	** @returns
	**   The number of records found.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public long
	findKeys(long[] keys) throws IMuException
	{
		if(call("findKeys", keys) instanceof Number count)
			return count.longValue();
		else
			throw new IMuException(call("findKeys", keys) + " cannot be converted to a Number");
	}

	/*!
	** Searches for records with key values in the array ``keys``.
	**
	** @param keys
	**   An array of keys being searched for.
	**
	** @returns
	**   The numeber of records found.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public long
	findKeys(ArrayList<Long> keys) throws IMuException
	{
		if(call("findKeys", keys.toArray()) instanceof Number count)
			return count.longValue();
		else
			throw new IMuException(call("findKeys", keys.toArray()) + " cannot be converted to a Number");
	}

	/*!
	** Searches for records which match the search terms specified in ``terms``.
	** 
	** @param terms
	**   The search terms.
	**
	** @returns
	**   An estimate of the number of records found.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public long
	findTerms(Terms terms) throws IMuException
	{
		if(call("findTerms", terms.toArray()) instanceof Number count)
			return count.longValue();
		else
			throw new IMuException(call("findTerms", terms.toArray()) + " cannot be converted to a Number");
	}

	/*!
	** Searches for recordes which match the TexQL ``WHERE`` clause.
	**
	** @param where 
	**   The TexQL ``WHERE`` clause to use.
	**
	** @returns 
	**   An estimate of the number of records found.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public long
	findWhere(String where) throws IMuException
	{
		if(call("findWhere", where) instanceof Number count)
			return count.longValue();
		else
			throw new IMuException(call("findWhere", where) + " cannot be converted to a Number");
	}

	public Map
	insert(Map values, String columns) throws IMuException
	{
		return doInsert(values, columns);
	}

	public Map
	insert(Map values, String[] columns) throws IMuException
	{
		return doInsert(values, columns);
	}

	public Map
	insert(Map values, ArrayList<String> columns) throws IMuException
	{
		return doInsert(values, columns.toArray());
	}

	public Map
	insert(Map values) throws IMuException
	{
		return doInsert(values, null);
	}

	public long
	remove(String flag, long offset, int count) throws IMuException
	{
		return doRemove(flag, offset, count);
	}

	public long
	remove(String flag, long offset) throws IMuException
	{
		return doRemove(flag, offset, null);
	}

	public long
	remove(ModuleFetchPosition pos, int count) throws IMuException
	{
		return doRemove(pos._flag, pos._offset, count);
	}

	public long
	remove(ModuleFetchPosition pos) throws IMuException
	{
		return doRemove(pos._flag, pos._offset, null);
	}

	/*!
	** Restores a set of records from a file on the server machine which 
	** contains a list of keys, one per line.
	**
	** @param file
	**   The file on the server machine containing the keys.
	**
	** @returns
	**   The number of records found.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public long
	restoreFromFile(String file) throws IMuException
	{
		Map args = new Map();
		args.put("file", file);
		return (Long) call("restoreFromFile", args);
	}

	/*!
	** Restores a set of records from a temporary file on the server machine 
	** which contains a list of keys, one per line. 
	** 
	** Operates the same way as **restoreFromFile( )**
	** [$<link>(:module:restoreFromFile)]  except that the file parameter is
	** relative to the server's temporary directory.
	**
	** @param file
	**   The file on the server machine containing the keys.
	**
	** @returns
	**   The number of records found.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public long
	restoreFromTemp(String file) throws IMuException
	{
		Map args = new Map();
		args.put("file", file);
		return (Long) call("restoreFromTemp", args);
	}
	
	/*!
	** Sorts the current result set by the sort keys in ``keys``.
	** 
	** Each sort key is a column name optionally preceded by a ``+``
	** (for an ascending sort) or a ``-`` (for a descending sort).
	** 
	** @param keys
	**   A `String` containing the list of sort keys. 
	**   The keys must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @param flags
	**   A `String` containing a set of flags specifying the behaviour of the sort.
	**   The flags must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @returns 
	**   A `ModuleSortResult` object. 
	**   If the ``report`` flag has not been specified the result will be 
	**   ``null`.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public ModuleSortResult
	sort(String keys, String flags) throws IMuException
	{
		return doSort(keys, flags);
	}
	
	/*!
	** Sorts the current result set by the sort keys in ``keys``.
	** 
	** Each sort key is a column name optionally preceded by a ``+``
	** (for an ascending sort) or a ``-`` (for a descending sort).
	** 
	** @param keys
	**   A `String` containing the list of sort keys. 
	**   The keys must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @param flags
	**   An array of `String`\s specifying the behaviour of the sort.
	**
	** @returns 
	**   A `ModuleSortResult` object. 
	**   If the ``report`` flag has not been specified the result will be ``null``.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public ModuleSortResult
	sort(String keys, String[] flags) throws IMuException
	{
		return doSort(keys, flags);
	}
	
	/*!
	** Sorts the current result set by the sort keys in ``keys``.
	**
	** Each sort key is a column name optionally preceded by a ``+``
	** (for an ascending sort) or a ``-`` (for a descending sort).
	** 
	** @param keys
	**   A `String` containing the list of sort keys. 
	**   The keys must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @param flags
	**   A list of `String`\s specifying the behaviour of the sort.
	**
	** @returns 
	**   A `ModuleSortResult` object. 
	**   If the ``report`` flag has not been specified the result will be 
	**   ``null``.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public ModuleSortResult
	sort(String keys, ArrayList<String> flags) throws IMuException
	{
		return doSort(keys, flags.toArray());
	}
	
	/*!
	** Sorts the current result set by the sort keys in ``keys``.
	**
	** Each sort key is a column name optionally preceded by a ``+``"
	** (for an ascending sort) or a ``-`` (for a descending sort).
	** 
	** @param keys
	**   An array of `String`\s containing the list of sort keys. 
	**
	** @param flags
	**   A `String` containing a set of flags specifying the behaviour of the sort.
	**   The flags must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @returns 
	**   A `ModuleSortResult` object. 
	**   If the ``report`` flag has not been specified the result will be 
	**   ``null``.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public ModuleSortResult
	sort(String[] keys, String flags) throws IMuException
	{
		return doSort(keys, flags);
	}

	/*!
	** Sorts the current result set by the sort keys in ``keys``.
	**
	** Each sort key is a column name optionally preceded by a ``+`` (for an
	** ascending sort) or a ``-`` (for a descending sort).
	**
	** @param keys
	**   An array of `String`\s containing the list of sort keys.
	**
	** @param flags
	**   An array of `String`\s specifying the behaviour of the sort.
	**
	** @returns
	**   A `ModuleSortResult` object.
	**   If the ``report`` flag has not been specified the result will be 
	**   ``null``.
	**
	**   @throws IMuException
	**     If a server-side error occurred.
	*/
	public ModuleSortResult
	sort(String[] keys, String[] flags) throws IMuException
	{
		return doSort(keys, flags);
	}
	
	/*!
	** Sorts the current result set by the sort keys in ``keys``.
	** Passed as an array list.
	**
	** Each sort key is a column name optionally preceded by a ``+`` (for an
	** ascending sort) or a ``-`` (for a descending sort).
	**
	** @param keys
	**   An array of `String`\s containing the list of sort keys.
	**
	** @param flags
	**   A list of `String`\s specifying the behaviour of the sort.
	**
	** @returns
	**   A `ModuleSortResult` object.
	**   If the ``report`` flag has not been specified the result will be 
	**   ``null``.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public ModuleSortResult
	sort(String[] keys, ArrayList<String> flags) throws IMuException
	{
		return doSort(keys, flags.toArray());
	}
	
	/*!
	** Sorts the current result set by the sort keys in ``keys``.
	**
	** Each sort key in a column name optionally preceded by a ``+`` (for an
	** ascending sort) or a ``-`` (for a descending sort).
	**
	** @param keys
	**   A list of `String`\s containing the list of sort keys.
	**
	** @param flags
	**   A `String` contining a set of flags specifying the behaviour of 
	**   the sort.
	**   The flags must be separated by a ``semi-colon`` or a ``comma``.
	**
	** @returns
	**   A `ModuleSortResult` object.
	**   If the ``report`` flag has not been specified the result will be 
	**   ``null``.
	**
	**   @throws IMuException
	**     If a server-side error occurred.
	*/
	public ModuleSortResult
	sort(ArrayList<String> keys, String flags) throws IMuException
	{
		return doSort(keys.toArray(), flags);
	}

	/*!
	** Sorts the current result set by the sort keys in ``keys``.
	**
	** Each sort key is a column name optionally preceded by a ``+`` (for an
	** ascending sort) or a ``-`` (for a descending sort).
	**
	** @param keys
	**   A list of `String`\s containing the list of sort keys.
	**
	** @param flags
	**   An array of `String`\s specifying the behaviour of the sort.
	**
	** @returns
	**   A `ModuleSortResult` object.
	**   If the ``report`` flag has not been specified the result will be 
	**   ``null``.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public ModuleSortResult
	sort(ArrayList<String> keys, String[] flags) throws IMuException
	{
		return doSort(keys.toArray(), flags);
	}
	
	/*!
	** Sorts the current result set by the sort keys in ``keys``.
	**
	** Each sort key is a column name optionally preceded by a ``+`` (for an
	** ascending sort) or a ``-`` (for a descending sort).
	**
	** @param keys
	**   A list of `String`\s containing the list of sort keys.
	**
	** @param flags
	**   A list of `String`\s specifying the behaviour of the sort.
	**
	** @returns
	**   A `ModuleSortResult` object.
	**   If the ``report`` flag has not been specified the result will be 
	**   ``null``.
	**
	** @throws IMuException
	**   If a server-side error occurred.
	*/
	public ModuleSortResult
	sort(ArrayList<String> keys, ArrayList<String> flags) throws IMuException
	{
		return doSort(keys.toArray(), flags.toArray());
	}
	
	public ModuleSortResult
	sort(String keys) throws IMuException
	{
		return doSort(keys, null);
	}
	
	public ModuleSortResult
	sort(String[] keys) throws IMuException
	{
		return doSort(keys, null);
	}
	
	public ModuleSortResult
	sort(ArrayList<String> keys) throws IMuException
	{
		return doSort(keys.toArray(), null);
	}

	public ModuleFetchResult
	update(String flag, long offset, int count, Map values, String columns)
		throws IMuException
	{
		return doUpdate(flag, offset, count, values, columns);
	}

	public ModuleFetchResult
	update(String flag, int offset, int count, Map values, String[] columns)
		throws IMuException
	{
		return doUpdate(flag, offset, count, values, columns);
	}

	public ModuleFetchResult
	update(String flag, int offset, int count, Map values, ArrayList<String> columns)
		throws IMuException
	{
		return doUpdate(flag, offset, count, values, columns.toArray());
	}

	public ModuleFetchResult
	update(String flag, long offset, int count, Map values)
		throws IMuException
	{
		return doUpdate(flag, offset, count, values, null);
	}

	public ModuleFetchResult
	update(ModuleFetchPosition pos, int count, Map values, String columns)
		throws IMuException
	{
		return doUpdate(pos._flag, pos._offset, count, values, columns);
	}

	public ModuleFetchResult
	update(ModuleFetchPosition pos, int count, Map values, String[] columns)
		throws IMuException
	{
		return doUpdate(pos._flag, pos._offset, count, values, columns);
	}

	public ModuleFetchResult
	update(ModuleFetchPosition pos, int count, Map values, ArrayList<String> columns)
		throws IMuException
	{
		return doUpdate(pos._flag, pos._offset, count, values, columns.toArray());
	}

	public ModuleFetchResult
	update(ModuleFetchPosition pos, int count, Map values)
		throws IMuException
	{
		return doUpdate(pos._flag, pos._offset, count, values, null);
	}

	protected String _table;
	
	protected int
	doAddFetchSet(String name, Object columns) throws IMuException
	{
		Map args = new Map();
		args.put("name", name);
		args.put("columns", columns);
		Long count = (Long) call("addFetchSet", args);
		return count.intValue();
	}
	
	protected int
	doAddSearchAlias(String name, Object columns) throws IMuException
	{
		Map args = new Map();
		args.put("name", name);
		args.put("columns", columns);
		Long count = (Long) call("addSearchAlias", args);
		return count.intValue();
	}
	
	protected int
	doAddSortSet(String name, Object keys) throws IMuException
	{
		Map args = new Map();
		args.put("name", name);
		args.put("columns", keys);
		Long count = (Long) call("addSortSet", args);
		return count.intValue();
	}
	
	protected ModuleFetchResult
	doFetch(String flag, long offset, int count, Object columns)
		throws IMuException
	{
		Map args = new Map();
		args.put("flag", flag);
		args.put("offset", offset);
		args.put("count", count);
		if (columns != null)
			args.put("columns", columns);
		return makeFetchResult(call("fetch", args));
	}

	protected Map
	doInsert(Map values, Object columns) throws IMuException
	{
		Map args = new Map();
		args.put("values", values);
		if (columns != null)
			args.put("columns", columns);
		return (Map) call("insert", args);
	}

	protected long
	doRemove(String flag, long offset, Object count) throws IMuException
	{
		Map args = new Map();
		args.put("flag", flag);
		args.put("offset", offset);
		if (count != null)
			args.put("count", count);
		return (Long) call("remove", args);
	}
	
	protected ModuleSortResult
	doSort(Object columns, Object flags) throws IMuException
	{
		Map args = new Map();
		args.put("columns", columns);
		if (flags != null)
			args.put("flags", flags);
		return makeSortResult(call("sort", args));
	}
	
	protected ModuleFetchResult
	doUpdate(String flag, long offset, int count, Map values, Object columns)
		throws IMuException
	{
		Map args = new Map();
		args.put("flag", flag);
		args.put("offset", offset);
		args.put("count", count);
		args.put("values", values);
		if (columns != null)
			args.put("columns", columns);
		return makeFetchResult(call("update", args));
	}

	protected void
	initialise(String table)
	{
		_name = "Module";
		_create = table;
		
		_table = table;
	}

	protected ModuleFetchResult
	makeFetchResult(Object raw)
	{
		Map data = (Map) raw;
		
		ModuleFetchResult result = new ModuleFetchResult();
		result._hits = data.getLong("hits");
		result._rows = data.getArray("rows", Map[].class);
		result._count = result._rows.length;		
		return result;
	}
	
	protected ModuleSortResult
	makeSortResult(Object raw)
	{
		if (raw == null)
			return null;
		ModuleSortResult result = new ModuleSortResult();
		Object[] list = (Object[]) raw;
		result._count = list.length;
		result._terms = new ModuleSortTerm[list.length];
		for (int i = 0; i < list.length; i++)
		{
			ModuleSortTerm term = new ModuleSortTerm();
			Map map = (Map) list[i];
			term._value = map.getString("value");
			term._count = map.getLong("count");
			if (map.containsKey("list"))
				term._nested = makeSortResult(map.get("list"));
			else
				term._nested = null;
			
			result._terms[i] = term;
		}
		return result;
	}
}
