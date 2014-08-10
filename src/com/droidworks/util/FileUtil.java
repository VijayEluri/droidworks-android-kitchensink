/**
 * Copyright 2010 Jason L. Hudgins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.droidworks.util;

import java.io.*;

/**
 * @author jasonhudgins
 *
 */
public class FileUtil {

	public static boolean exists(String path)  {
		File f = new File(path);
		return f.exists();
	}


    /**
     * Dump an input stream to a file, throws exceptions that you will likely want to handle.
     *
     * @param file
     * @param inputStream
     * @throws IOException
     */
    public static void dumpToFile(File file, InputStream inputStream) throws IOException {

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fios =  new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int len = inputStream.read(buffer);
        while (len != -1) {
            fios.write(buffer, 0, len);
            len = inputStream.read(buffer);
        }

        fios.close();
        inputStream.close();
    }

}
