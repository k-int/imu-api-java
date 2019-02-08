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

import java.io.IOException;
import java.io.FilterInputStream;
import java.io.InputStream;

class MixedInputStream extends FilterInputStream
{
	public
	MixedInputStream(InputStream in)
	{
		super(in);
	}
	
	public int 
	readChar() throws IOException, IMuException
	{
		int v = 0;
		int n = 0;
		int b = read();
		if (b < 0)
			return -1;
		if ((b & 0x80) == 0)
		{
			v = b & 0x7F;
			n = 0;
		}
		else if ((b & 0xE0) == 0xC0)
		{
			v = b & 0x1F;
			n = 1;
		}
		else if ((b & 0xF0) == 0xE0)
		{
			v = b & 0x0F;
			n = 2;
		}
		/* Multi-character input not supported */
		else
			throw new IMuException("InputCharacterStart", b);
		
		for (int i = 0; i < n; i++)
		{
			b = read();
			if (b < 0)
				throw new IMuException("InputCharacterTruncated");
			if ((b & 0xC0) != 0x80)
				throw new IMuException("InputCharacterCorrupted");
			v <<= 6;
			v |= b & 0x3F;
		}
		
		char[] chars = Character.toChars(v);
		if (chars.length != 1)
			throw new IMuException("InputCharacterMulti");
		return (int) chars[0];
	}
}
