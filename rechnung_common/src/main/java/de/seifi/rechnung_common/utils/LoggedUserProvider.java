package de.seifi.rechnung_common.utils;

import java.util.UUID;

public class LoggedUserProvider {

    private static UUID loggedUser = null;

    public static UUID getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(UUID loggedUser) {
        LoggedUserProvider.loggedUser = loggedUser;
    }
}
