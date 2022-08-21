package de.seifi.rechnung_common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

class ProcessChecker
{
    static boolean isStillAllive(String pidStr)
    {
        String OS = System.getProperty("os.name").toLowerCase();
        String command;
        if (OS.contains("win"))
        {
            command = "cmd /c tasklist /FI \"PID eq " + pidStr + "\"";
        }
        else if (OS.contains("nix") || OS.contains("nux"))
        {
            command = "ps -p " + pidStr;
        }
        else
        {
            return false;
        }
        return isProcessIdRunning(pidStr, command); // call generic implementation
    }

    private static boolean isProcessIdRunning(String pid, String command)
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);

            InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
            BufferedReader bReader = new BufferedReader(isReader);
            String strLine;
            while ((strLine = bReader.readLine()) != null)
            {
                if (strLine.contains(" " + pid + " "))
                {
                    return true;
                }
            }

            return false;
        }
        catch (Exception ex)
        {
            return true;
        }
    }
}

