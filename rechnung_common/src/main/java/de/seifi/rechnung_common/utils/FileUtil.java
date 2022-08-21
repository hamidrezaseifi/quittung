package de.seifi.rechnung_common.utils;

import java.io.*;
import java.nio.charset.Charset;

class FileUtil
{
    static void createDirectories(String dirPath) throws IOException
    {
        File dir = new File(dirPath);
        if (dir.mkdirs())   /* If false, directories already exist so nothing to do. */
        {
            if (!dir.exists())
            {
                throw new IOException(String.format("Failed to create directory (access permissions problem?): [%s]", dirPath));
            }
        }
    }

    static void createFile(String fullPathToFile, String contents)
    {
        try (PrintWriter writer = new PrintWriter(fullPathToFile, Charset.defaultCharset()))
        {
            writer.print(contents);
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Unable to create file at %s! %s", fullPathToFile, e.getMessage()), e);
        }
    }

    static String readFile(File file)
    {
        try
        {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file)))
            {
                StringBuilder result = new StringBuilder();

                String line;
                while ((line = fileReader.readLine()) != null)
                {
                    result.append(line);
                    if (fileReader.ready())
                        result.append("\n");
                }
                return result.toString();
            }
        }
        catch (IOException e)
        {
            return null;
        }
    }
}