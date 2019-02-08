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
** This class is used to create a set of search terms that is passed to the IMu 
** server. 
**
** A `Terms` object can be passed to the **findTerms( )** 
** [$<link>(:module:findTerms)] method of either a `Module`
** [$<link>(:module:module)] or `Modules` object.
**
** @usage
**   com.kesoftware.imu.Terms
** @end
**
** @since 1.0
*/
public class Terms
{
	/* Constructors */
	/*!
	** Creates a new `Terms` object with the given ``kind``. 
	**
	** The ``kind`` can be either:
	** 		* ``TermsKind.AND`` (for a set of AND terms) 
	** 		-OR- 
	** 		* ``TermsKind.OR`` (for a set of OR terms)
	*/
	public
	Terms(TermsKind kind)
	{
		_kind = kind;
		_list = new ArrayList<Object>();
	}
	
	/*!
	** Creates a new AND `Terms` object.
	**
	** This is the equivalent of:
	** @code
	**   Terms(TermsKind.AND)
	*/
	public
	Terms()
	{
		this(TermsKind.AND);
	}
	
	/* Properties */
	/*!
	** @property kind
	**   The kind of terms list as specified when the object was constructed.
	**   Will be either:
	**   * ``TermsKind.AND``
	**   -OR-
	**   * ``TermsKind.OR``
	*/
	public TermsKind
	getKind()
	{
		return _kind;
	}
	
	/*!
	** @property list
	**   The list of search terms themselves.
	**   Each element in the list can be either:
	**   * A two or three element array comprising:
	**     - a column name
	**     - text to search for
	**     - an optional operator
	**   * A nested $<Terms> object
	*/
	public Object[]
	getList()
	{
		return _list.toArray();
	}
	
	/* Methods */
	/*!
	** Adds a new term to the list.
	**
	** @param name
	**   The name of a column or a search alias.
	**
	** @param value
	**   The value to match.
	**
	** @param operator
	**   An operator to apply (such as ``contains``, ``=``, ``<``, etc.) for the
	**   server to apply when searching.
	*/
	public void
	add(String name, String value, String operator)
	{
		Object[] term = { name, value, operator};
		_list.add(term);
	}
	
	/*!
	** Adds a new term to the list.
	**
	** This is the preferred method for adding terms in many cases as it allows
	** the server to choose the most suitable operator.
	**
	** @param name
	**   The name of a column or a search alias.
	**
	** @param value
	**   The value to match.
	*/
	public void
	add(String name, String value)
	{
		add(name, value, null);
	}
	
	/*!
	** Adds an initially empty nested set of AND terms to the list.
	**
	** This is a shortcut for:
	** @code
	**   addTerms(TermsKind.AND)
	**
	** @returns
	**   The newly added `Terms` object.
	*/
	public Terms
	addAnd()
	{
		return addTerms(TermsKind.AND);
	}
	
	/*!
	** Adds an initially empty nested set of OR terms to the list.
	**
	** This is a shortcut for:
	** @code
	**   addTerms(TermsKind.OR)
	**
	** @returns
	**   The newly added `Terms` object.
	*/
	public Terms
	addOr()
	{
		return addTerms(TermsKind.OR);
	}
	
	/*!
	** Adds an initially empty nested set of terms to the list.
	**
	** @returns
	**   The newly added `Terms` object.
	*/
	public Terms
	addTerms(TermsKind kind)
	{
		Terms child = new Terms(kind);
		_list.add(child);
		return child;
		
	}
	
	public Object[]
	toArray()
	{
		Object[] result = new Object[2];
		
		result[0] = _kind.name();
		
		int size = _list.size();
		Object[] list = new Object[size];
		for (int i = 0; i < size; i++)
		{
			Object term = _list.get(i);
			if (term instanceof Terms)
				term = ((Terms) term).toArray();
			list[i] = term;
		}
		result[1] = list;
		
		return result;
	}

	private TermsKind _kind;
	private ArrayList<Object> _list;
}
