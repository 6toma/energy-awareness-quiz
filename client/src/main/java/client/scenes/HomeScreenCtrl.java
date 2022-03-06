package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private TextField inputTitleField;

    @FXML
    private Label titleField;

    @FXML
    private Button darkMode;

    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isLightMode = true;
    }

    @FXML
    void toggleDarkMode() {
        isLightMode = !isLightMode;
        if(!isLightMode)
            darkMode.setText("Light Mode");
        else {
            darkMode.setText("Dark Mode");
        }
        mainCtrl.checkDarkMode();
    }

    public boolean getDarkMode() {
        return isLightMode;
    }

    @FXML
    void changeTitleButtonClicked(ActionEvent event) {
        String newUser = inputTitleField.getText();
        if (newUser.length() > 0)
            titleField.setText("Hello, " + newUser + "!");
    }

    @FXML
    void exitApp(ActionEvent event) {
        // to fully terminate the client process
        Platform.exit();
        System.exit(0);
    }

    public void showWaitingRoom() {
        mainCtrl.showWaitingRoom();
    }

    public void showLoadingScreen() {
        mainCtrl.showLoadingScreen();
    }
}
