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


    @FXML
    private Button changeTitleButton;

    @FXML
    private TextField inputTitleField;

    @FXML
    private Label titleField;

    @FXML
    private Button exitButton;

    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    @FXML
    void changeTitleButtonClicked(ActionEvent event) {
        String newUser = inputTitleField.getText();
        if (newUser.length() > 0)
            titleField.setText("Hello, " + newUser + "!");
    }

    @FXML
    void exitApp(ActionEvent event) {
        //Stage stage = (Stage) exitButton.getScene().getWindow();
        //stage.close();

        // to fully terminate the client process
        Platform.exit();
        System.exit(0);
    }

    public void showWaitingRoom() {
        mainCtrl.showWaitingRoom();
    }
}
