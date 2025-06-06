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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.Cleaner;
import java.util.Objects;


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
public class TempInputStream extends FileInputStream {

    private static final Cleaner cleaner = Cleaner.create();

    private final File file;
    private final Cleaner.Cleanable cleanable;

    /**
     * Creates an input stream for the specified temporary file.
     * The file will be deleted when the stream is closed or finalized.
     *
     * @param file the temporary file to access
     * @throws FileNotFoundException if the file doesn't exist
     */
    public TempInputStream(File file) throws FileNotFoundException {
        super(Objects.requireNonNull(file));
        this.file = file;
        this.cleanable = cleaner.register(this, new FileCleanup(file));
    }

    /**
     * Closes the stream and deletes the file.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        super.close();
        cleanable.clean(); // Triggers cleanup immediately
    }

    /**
     * Runnable class used by the Cleaner to delete the file.
     */
    private static class FileCleanup implements Runnable {
        private final File file;

        FileCleanup(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }
}