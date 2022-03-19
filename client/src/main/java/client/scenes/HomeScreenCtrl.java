package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


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

    @FXML
    private TextField inputServerURLField;

    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isLightMode = true;
        this.usernameOriginScreen = 0; //still on home screen
    }

//    @FXML
//    void toggleDarkMode() {
//        isLightMode = !isLightMode;
//        if (!isLightMode)
//            darkMode.setText("Light Mode");
//        else {
//            darkMode.setText("Dark Mode");
//        }
//        mainCtrl.checkDarkMode();
//    }

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

    /**
     * This method exits the app
     * @param event
     */
    @FXML
    void exitApp(ActionEvent event) {
        // to fully terminate the client process
        Platform.exit();
        System.exit(0);
    }

    /**
     * This method transfers the user to the settings screen
     * where he/she can switch to dark mode, read the help page,
     * enter the room URL or go to admin panel
     * @param event
     */
    @FXML
    void goToSettings(ActionEvent event) {
        mainCtrl.showSettingsScreen();
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

    /**
     * Tries to get a question from the server
     * If succeeds connect create a new singlePlayerGame and go to the username screen
     * <p>
     * TODO:
     * - Show error to user if connection to server failed
     */
    @FXML
    public void showUsernameScreenSingle() {

        mainCtrl.getServer().setServerURL(inputServerURLField.getText());
        try {
            mainCtrl.newSinglePlayerGame();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Connection failed");
        }
    }

    @FXML
    public void showUsernameScreenMulti() {

        // Testing ServerUtils postPlayer functionality
        System.out.println(inputServerURLField.getText());
        server.setServerURL(inputServerURLField.getText());
        Player player = new Player("test", 7357);
        try {
            System.out.println(server.postPlayer(player));
        } catch (Exception e) {
            System.err.println("Connection failed");
        }

        mainCtrl.setUsernameOriginScreen(2);
        mainCtrl.showUsernameScreen();
    }

}
