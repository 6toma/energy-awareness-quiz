package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;

import java.util.List;


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


    @Getter
    public int usernameOriginScreen;

    // @FXML
    // private Text titleText;
    // could be used to style the title

    @FXML
    private Button darkMode;

    @FXML
    private TextField inputServerURLField;

    @FXML
    private Label playerLabel1, playerLabel2;

    @FXML
    private Label scoreLabel1, scoreLabel2;

    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isLightMode = true;
        this.usernameOriginScreen = 0; //still on home screen
    }

    /**
     * Gets the value of isLightMode
     * @return false if is in dark mode (??)
     */
    public boolean getDarkMode() {
        return isLightMode;
    }

    /**
     * Sets the origin of the username selection screen
     * @param usernameOriginScreen 1 if going to single player
     *                             2 if going to multiplayer
     */
    public void setUsernameOriginScreen(int usernameOriginScreen) {
        if (usernameOriginScreen == 0 ||
                usernameOriginScreen == 1 ||
                usernameOriginScreen == 2)
            this.usernameOriginScreen = usernameOriginScreen;
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

    /**
     * Used to update leaderboard entries
     * Method is meant to be used paired with
     * a GET request from the server in order
     * to get the current top 10 list
     *
     * current method body is a placeholder used
     * to get a feel of the scope of the method
     */
    @FXML
    public void setPlayer() {
        List<String> list = List.of("Matt", "Coolguy123", "Gamewinner_xX", "he who shall not be named", "bro");
        int randomName = (int) Math.floor(Math.random() * list.size());
        playerLabel1.setText(list.get(randomName));
        Integer randomScore = (int) Math.floor(Math.random() * 250) + 5000;
        scoreLabel1.setText(randomScore.toString());
    }


    /**
     * Tries to get a question from the server
     * If succeeds connect create a new singlePlayerGame and go to the username screen
     * <p>
     * TODO:
     * - Show error to user if connection to server failed
     */
    @FXML
    public void showUsernameScreenSingle() {

        mainCtrl.getServer().setServerURL(mainCtrl.getServerURL());
        try {
            mainCtrl.newSinglePlayerGame();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Connection failed");
        }
    }

    /**
     * Run when pressed multiplayer
     * Sends you to the username selection screen
     */
    @FXML
    public void showUsernameScreenMulti() {

        // Testing ServerUtils postPlayer functionality
        System.out.println(mainCtrl.getServerURL());
        server.setServerURL(mainCtrl.getServerURL());
        Player player = new Player("test", 7357);
        try {
            System.out.println(server.postPlayer(player));
        } catch (Exception e) {
            System.err.println("Connection failed");
        }

        mainCtrl.setUsernameOriginScreen(2);
        mainCtrl.showUsernameScreen();
    }

    /**
     * Shows the help screen
     */
    @FXML
    public void showHelpScreen() {
        mainCtrl.showHelpScreen();
    }

    @FXML
    void exitApp(ActionEvent event) {
        // to fully terminate the client process
        Platform.exit();
        System.exit(0);
    }
}
