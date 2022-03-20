package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SettingsScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public boolean isLightMode;

    @Inject
    public SettingsScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    private Button darkMode;

    @FXML
    private TextField inputServerURLField;

    @FXML
    void toggleDarkMode() {
        isLightMode = !isLightMode;
        if (isLightMode)
            darkMode.setText("Light Mode");
        else {
            darkMode.setText("Dark Mode");
        }
        mainCtrl.checkDarkMode();
    }

    @FXML
    public void showHelpScreen() {
        mainCtrl.showHelpScreen();
    }

    public boolean getDarkMode() {
        return isLightMode;
    }

    public void exit() {
        mainCtrl.showHomeScreen();
    }

    public String getServerURL() {
        return inputServerURLField.getText();
    }
}
