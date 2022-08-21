package de.seifi.rechnung_common.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class RunSingleInstance {

    public static void runInstance(String[] args, ISingleInstanceRunnable singleInstanceRunnable, String appId) {

        if (!isOnlyInstanceOf(appId))
        {
            System.exit(-1);
        }

        singleInstanceRunnable.runInstance(args);

    }

    public static boolean isOnlyInstanceOf(String componentName)
    {
        boolean result = false;

        // Make sure the directory exists
        String dirPath = getHomePath();
        try
        {
            FileUtil.createDirectories(dirPath);
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Unable to create directory: [%s]", dirPath));
        }

        File pidFile = new File(dirPath, componentName + ".pid");

        // Try to read a prior, existing pid from the pid file. Returns null if the file doesn't exist.
        String oldPid = FileUtil.readFile(pidFile);

        if (oldPid != null && ProcessChecker.isStillAllive(oldPid))
        {

        }
        else
        {
            // Write current pid to the file.
            long thisPid = ProcessHandle.current().pid();
            FileUtil.createFile(pidFile.getAbsolutePath(), String.valueOf(thisPid));

            // Try to be tidy. Note: This won't happen on exit if forcibly terminated, so we don't depend on it.
            pidFile.deleteOnExit();

            result = true;
        }

        return result;
    }

    public static String getHomePath()
    {
        // Returns a path like C:/Users/Person/
        return System.getProperty("user.home") + "/";
    }


}
