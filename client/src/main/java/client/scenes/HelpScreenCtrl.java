package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class HelpScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    // 1 - Home Screen
    // 2 - Set Username
    // 3 - Waiting Room
    // ...
    public int previousScreen;


    @Inject
    public HelpScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void back() {
        if (previousScreen == 1)
            mainCtrl.showHomeScreen();
        else if (previousScreen == 2)
            mainCtrl.showUsernameScreen();
        else if (previousScreen == 3)
            mainCtrl.showWaitingRoom();
    }

    public void setPreviousScreen(int previousScreen) {
        if (previousScreen == 1 || previousScreen == 2 || previousScreen == 3)
            this.previousScreen = previousScreen;

    }
}
