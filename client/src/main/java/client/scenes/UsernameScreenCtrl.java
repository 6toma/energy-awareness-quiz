package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UsernameScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField inputUsernameField;

    @FXML
    private Label usernameField;

    @FXML
    private Button continueButton;

    @Inject
    public UsernameScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * This is an alternative to binding the text properties
     * of the text field and the title text
     *
     * IMO we should keep this because we can easily check
     * the username before allowing it to be set, whereas
     * with binding the "Hello, x!" will appear regardless
     * of the new username, which may be invalid
     */
    @FXML
    void setUsernameButtonClicked(ActionEvent event) {
        String newUser = inputUsernameField.getText();
        if (newUser.length() > 0) { // in the future to be replaced with a isValidUsername(newUser) type function
            continueButton.setDisable(false);
            usernameField.setText("Hello, " + newUser + "!");
        }
    }

    @FXML
    void userInputOnEnter(ActionEvent event) {
        setUsernameButtonClicked(event);
    }

    @FXML
    void back(ActionEvent event) {
        resetUserText();
        mainCtrl.showHomeScreen();
    }

    @FXML
    void showNextScreen(ActionEvent event) {
        if(mainCtrl.getUsernameOriginScreen() == 1) {
            mainCtrl.showLoadingScreen();
        } else {
            mainCtrl.showWaitingRoom();
        }
        resetUserText();
    }

    public void resetUserText() {
        usernameField.setText("Please input your username!");
        inputUsernameField.clear();
        continueButton.setDisable(true);
    }

}
