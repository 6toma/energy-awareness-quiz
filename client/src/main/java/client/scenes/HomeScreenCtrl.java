package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


/**
 * Controller used as an intermediate between controllers for all
 * scenes/windows
 * <p>
 * CURRENTLY, USED FOR HOME SCREEN/SCENE ONLY
 */
public class HomeScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    public boolean isLightMode;

    //0 - still on home screen
    //1 - reached username screen by pressing SINGLEPLAYER
    //2 - reached username screen by pressing MULTIPLAYER
    public int usernameOriginScreen;

    // @FXML
    // private Text titleText;
    // could be used to style the title

    @FXML
    private Button darkMode;


    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isLightMode = true;
        this.usernameOriginScreen = 0; //still on home screen
    }

    @FXML
    void toggleDarkMode() {
        isLightMode = !isLightMode;
        if (!isLightMode)
            darkMode.setText("Light Mode");
        else {
            darkMode.setText("Dark Mode");
        }
        mainCtrl.checkDarkMode();
    }

    public boolean getDarkMode() {
        return isLightMode;
    }

    public int getUsernameOriginScreen() {
        return usernameOriginScreen;
    }

    public void setUsernameOriginScreen(int usernameOriginScreen) {
        if (usernameOriginScreen == 0 ||
                usernameOriginScreen == 1 ||
                usernameOriginScreen == 2)
            this.usernameOriginScreen = usernameOriginScreen;
    }

    @FXML
    void exitApp(ActionEvent event) {
        // to fully terminate the client process
        Platform.exit();
        System.exit(0);
    }

    /*
    @FXML
    public void showWaitingRoom() {
        mainCtrl.showWaitingRoom();
    }

    @FXML
    public void showLoadingScreen() {
        mainCtrl.showLoadingScreen();
    }
    */

    @FXML
    public void showUsernameScreenSingle() {
        mainCtrl.setUsernameOriginScreen(1);
        mainCtrl.showUsernameScreen();
    }

    @FXML
    public void showUsernameScreenMulti() {
        mainCtrl.setUsernameOriginScreen(2);
        mainCtrl.showUsernameScreen();
    }
}
