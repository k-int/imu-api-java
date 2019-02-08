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

public class Modules extends Handler
{
	/* Constructors */
	public Modules(Session session)
	{
		super(session);
		initialise();
	}

	public Modules()
	{
		super();
		initialise();
	}
	
	/* Methods */
	public int
	addFetchSet(String name, Map set) throws IMuException
	{
		Map args = new Map();
		args.put("name", name);
		args.put("set", set);
		Long count = (Long) call("addFetchSet", args);
		return count.intValue();
	}
	
	public int
	addFetchSets(Map sets) throws IMuException
	{
		Long count = (Long) call("addFetchSets", sets);
		return count.intValue();
	}
	
	public int
	addSearchAlias(String name, Map set) throws IMuException
	{
		Map args = new Map();
		args.put("name", name);
		args.put("set", set);
		Long count = (Long) call("addSearchAlias", args);
		return count.intValue();
	}
	
	public int
	addSearchAliases(Map aliases) throws IMuException
	{
		Long count = (Long) call("addSearchAliases", aliases);
		return count.intValue();
	}
	
	public int
	addSortSet(String name, Map set) throws IMuException
	{
		Map args = new Map();
		args.put("name", name);
		args.put("set", set);
		Long count = (Long) call("addSortSet", args);
		return count.intValue();
	}
	
	public int
	addSortSets(Map sets) throws IMuException
	{
		Long count = (Long) call("addSortSets", sets);
		return count.intValue();
	}

	public ModulesFetchResult
	fetch(String flag, long offset, int count, String columns)
		throws IMuException
	{
		return doFetch(flag, offset, count, columns);
	}

	public ModulesFetchResult
	fetch(String flag, long offset, int count)
		throws IMuException
	{
		return doFetch(flag, offset, count, null);
	}

	public ModulesFetchResult
	fetch(ModulesFetchPosition pos, int count, String columns)
		throws IMuException
	{
		return doFetch(pos._flag, pos._offset, count, columns);
	}

	public ModulesFetchResult
	fetch(ModulesFetchPosition pos, int count)
		throws IMuException
	{
		return doFetch(pos._flag, pos._offset, count, null);
	}

	public long
	findAttachments(String table, String column, long key) throws IMuException
	{
		Map args = new Map();
		args.put("table", table);
		args.put("column", column);
		args.put("key", key);
		return (Long) call("findAttachments", args);
	}

	public String[]
	findKeys(ModulesKey[] keys, String[] include) throws IMuException
	{
		return doFindKeys(keys, include);
	}

	public String[]
	findKeys(ModulesKey[] keys, String include) throws IMuException
	{
		return doFindKeys(keys, include);
	}

	public String[]
	findKeys(ModulesKey[] keys) throws IMuException
	{
		return doFindKeys(keys, null);
	}

	public String[]
	findKeys(ModulesKeys keys, String[] include) throws IMuException
	{
		return doFindKeys(keys.toArray(), include);
	}

	public String[]
	findKeys(ModulesKeys keys, String include) throws IMuException
	{
		return doFindKeys(keys.toArray(), include);
	}

	public String[]
	findKeys(ModulesKeys keys) throws IMuException
	{
		return doFindKeys(keys.toArray(), null);
	}

	public String[]
	findTerms(Terms terms, String[] include) throws IMuException
	{
		return doFindTerms(terms, include);
	}

	public String[]
	findTerms(Terms terms, String include) throws IMuException
	{
		return doFindTerms(terms, include);
	}

	public String[]
	findTerms(Terms terms) throws IMuException
	{
		return doFindTerms(terms, null);
	}
	
	public long
	getHits(String module) throws IMuException
	{
		return (Long) call("getHits", module);
	}
	
	public long
	getHits() throws IMuException
	{
		return (Long) call("getHits");
	}

	public long
	restoreFromFile(String file) throws IMuException
	{
		Map args = new Map();
		args.put("file", file);
		return (Long) call("restoreFromFile", args);
	}

	public long
	restoreFromFile(String file, String module) throws IMuException
	{
		Map args = new Map();
		args.put("file", file);
		args.put("module", module);
		return (Long) call("restoreFromFile", args);
	}

	public long
	restoreFromTemp(String file) throws IMuException
	{
		Map args = new Map();
		args.put("file", file);
		return (Long) call("restoreFromTemp", args);
	}

	public long
	restoreFromTemp(String file, String module) throws IMuException
	{
		Map args = new Map();
		args.put("file", file);
		args.put("module", module);
		return (Long) call("restoreFromTemp", args);
	}
	
	public int
	setModules(String[] modules) throws IMuException
	{
		return (Integer) call("setModules", modules);
	}
	
	public int
	setModules(String modules) throws IMuException
	{
		return (Integer) call("setModules", modules);
	}
	
	public int
	setModules() throws IMuException
	{
		return (Integer) call("setModules");
	}
	
	public Object
	sort(Map set, String[] flags) throws IMuException
	{
		return doSort(set, flags);
	}
	
	public Object
	sort(Map set, String flags) throws IMuException
	{
		return doSort(set, flags);
	}
	
	public Object
	sort(Map set) throws IMuException
	{
		return doSort(set, null);
	}
	
	protected ModulesFetchResult
	doFetch(String flag, long offset, int count, String columns)
		throws IMuException
	{
		Map args = new Map();
		args.put("flag", flag);
		args.put("offset", offset);
		args.put("count", count);
		if (columns != null)
			args.put("columns", columns);
		Map data = (Map) call("fetch", args);
		
		ModulesFetchResult result = new ModulesFetchResult();
		result._count = data.getInt("count");
		
		Map[] list = data.getMaps("modules");
		result._modules = new ModulesFetchModule[list.length];
		for (int i = 0; i < list.length; i++)
		{
			Map map = list[i];
			ModulesFetchModule module = new ModulesFetchModule();
			module._hits = map.getLong("hits");
			module._index = map.getInt("index");
			module._name = map.getString("name");
			module._rows = map.getArray("rows", Map[].class);
			
			result._modules[i] = module;
		}
		
		if (data.containsKey("current"))
			result._current = makePosition(data.get("current"));
		if (data.containsKey("prev"))
			result._prev = makePosition(data.get("prev"));
		if (data.containsKey("next"))
			result._next = makePosition(data.get("next"));
		
		return result;
	}
	
	protected String[]
	doFindKeys(ModulesKey[] keys, Object include) throws IMuException
	{
		Map args = new Map();
		
		Object[] list = new Object[keys.length];
		for (int i = 0; i < keys.length; i++)
		{
			Object[] item = { keys[i].getModule(), keys[i].getKey() };
			list[i] = item;
		}
		args.put("keys", list);
		
		if (include != null)
			args.put("include", include);
		
		Object[] data = (Object[]) call("findKeys", args);
		return Arrays.copyOf(data, data.length, String[].class);
	}
	
	protected String[]
	doFindTerms(Terms terms, Object include) throws IMuException
	{
		Map args = new Map();
		args.put("terms", terms.toArray());
		if (include != null)
			args.put("include", include);
		Object[] data = (Object[]) call("findTerms", args);
		return Arrays.copyOf(data, data.length, String[].class);
	}
	
	protected Object
	doSort(Map set, Object flags) throws IMuException
	{
		Map args = new Map();
		args.put("set", set);
		if (flags != null)
			args.put("flags", flags);
		return call("sort", args);
	}

	protected void
	initialise()
	{
		_name = "Modules";
	}
	
	protected ModulesFetchPosition
	makePosition(Object raw)
	{	
		Map map = (Map) raw;
		String flag = map.getString("flag");
		long offset = map.getLong("offset");
		return new ModulesFetchPosition(flag, offset);
	}
}
