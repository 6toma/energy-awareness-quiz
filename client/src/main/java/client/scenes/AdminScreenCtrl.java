package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class AdminScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Creates a new screen with injections
     *
     * @param server   ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public AdminScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Returns to the home screen
     */
    public void goToHomeScreen() {
        mainCtrl.showHomeScreen();
    }

}
