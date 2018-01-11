/*
 * Copyright © 2018. Sir Wellington.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.sirwellington.alchemy.http;

import java.io.*;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.arguments.Required;

/**
 * @author SirWellington
 */
@Internal
final class ByteStreams
{

    private static final Logger LOG = LoggerFactory.getLogger(ByteStreams.class);

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int END_OF_FILE = -1;

    /**
     * Reads all of the bytes in {@code istream} and
     *
     * @param istream The input stream to read. This function does not close the input stream.
     * @return A {@code byte[]} containing all of the data in {@copde istream}.
     * @throws IOException If any of the underlying operations fail.
     */
    static byte[] toByteArray(InputStream istream) throws IOException
    {
        try (ByteArrayOutputStream ostream = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);)
        {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

            int read = istream.read(buffer);

            while (read != END_OF_FILE)
            {
                ostream.write(buffer, 0, read);
                read = istream.read(buffer);
            }

            ostream.flush();

            return ostream.toByteArray();
        }
    }

    static byte[] toByteArray(@Required URL url) throws IOException
    {
        try (InputStream istream = url.openStream())
        {
            return toByteArray(istream);
        }
    }
}
