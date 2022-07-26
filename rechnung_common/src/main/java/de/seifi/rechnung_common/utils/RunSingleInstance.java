package de.seifi.rechnung_common.utils;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import it.sauronsoftware.junique.MessageHandler;

public class RunSingleInstance {

    public static void runInstance(String[] args, ISingleInstanceRunnable singleInstanceRunnable, String appId) {

        boolean alreadyRunning;
        try {
            JUnique.acquireLock(appId, new MessageHandler() {
                public String handle(String message) {

                    return null;
                }
            });
            alreadyRunning = false;
        } catch (AlreadyLockedException e) {
            alreadyRunning = true;
        }
        if (!alreadyRunning) {

            singleInstanceRunnable.runInstance(args);

        } else {
            JUnique.sendMessage(appId, "Anwendung läuft bereits");
            System.exit(-1);

        }

    }

}
