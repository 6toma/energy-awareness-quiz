package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button changeTitleButton;

    @FXML
    private TextField inputTitleField;

    @FXML
    private Label titleField;

    @FXML
    private Button exitButton;

    @FXML
    void changeTitleClicked(ActionEvent event) {
        String newUser = inputTitleField.getText();
        titleField.setText("Hello, " + newUser + "!");
    }

    @FXML
    void exitApp(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

}
