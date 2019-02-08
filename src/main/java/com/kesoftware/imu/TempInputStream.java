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

/*!
** This class is used to provide access to a temporary copy of a file
** returned from the server.
**
** This is most commonly used when a request is made to fetch a virtual 
** multimedia column such as ``resource``.
**
** The class ensures that the temporary file that the stream is accessing is 
** removed when the stream is closed or the stream object is finalised.
**
** @usage
**   com.kesoftware.imu.TempInputStream
** @end
**
** @extends java.io.FileInputStream
**
** @since 1.0
*/
class TempInputStream extends FileInputStream
{
	/* Constructors */
	/*!
	** Creates an input stream to access the file referred to by file.
	** Note: this file is removed when the steam is closed or finalised.
	**
	** @param file
	**   Temporary file to be accessed.
	**
	** @throws FileNotFoundException.
	**   If the file does not exist.
	*/
	public
	TempInputStream(File file) throws FileNotFoundException
	{
		super(file);
		_file = file;
	}
	
	/* Methods */
	/*!
	** Closes the input stream.
	**
	** The file associated with the stream is removed.
	**
	** @throws IOException
	**   If the stream access failed.
	*/
	public void
	close() throws IOException
	{
		super.close();
		_file.delete();
		_file = null;
	}
	
	/*!
	** Overrides the base class's **finalize( )** method.
	**
	** If close has not been called previously, the stream is closed and the 
	** file is removed.
	**
	** @throws IOException
	**   If the stream access failed.
	*/
	protected void
	finalize() throws IOException
	{
		super.finalize();
		if (_file != null)
			_file.delete();
	}
	
	private File _file;  
}
