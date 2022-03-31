package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
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

    // The point of this field is to check whether
    // a player has pressed the CONTINUE button in
    // the username screen, so that their name
    // gets saved, so they don't have to input it again
    private boolean usernameInUse = false;

    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
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

        // checking whether username is already in waiting room
        boolean isValidUsername = (Boolean) server.checkValidityOfUsername(newUser);
        if (isValidUsername) {
            continueButton.setDisable(false);
            usernameField.setText("Hello, " + newUser + "!");
        }
        else {
            continueButton.setDisable(true);
            usernameField.setText("Please select a different username!");
        }
    }

    @FXML
    void userInputOnEnter(ActionEvent event) {
        setUsernameButtonClicked(event);
    }

    @FXML
    void back(ActionEvent event) {
        continueButton.setDisable(true);
        resetUserText();
        mainCtrl.showHomeScreen();
    }

    @FXML
    void showNextScreen(ActionEvent event) {
        if(mainCtrl.getUsernameOriginScreen() == 1) {
            mainCtrl.getSinglePlayerGame().setPlayer(new Player(inputUsernameField.getText()));
            mainCtrl.showLoadingScreen();
        } else {

            //send player to multiplayer game object
            Player newPlayer = server.addPlayerWaitingRoom(new Player(inputUsernameField.getText()));
            if(newPlayer == null){
                usernameField.setText("Please select a different username!");
            }
            else {
                System.out.println(newPlayer);
                mainCtrl.showWaitingRoom();
                if(!server.areQuestionsGenerated()){
                    System.out.println("generate them ffs");;
                }
            }

        }
        // CONTINUE button has been pressed
        // so a username is now in use
        this.usernameInUse = true;
    }

    /**
     * Resets the text in the username field
     */
    public void resetUserText() {
        if(!usernameInUse) {
            usernameField.setText("Please input your username!");
            inputUsernameField.clear();
            continueButton.setDisable(true);
        }
    }

}
