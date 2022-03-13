package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EndScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Button playAgain;
    @FXML
    private Button goToHomeScreen;
    @FXML
    private Label scoreLabel;

    @Inject
    public EndScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }


    public void goToHomeScreen() {
        mainCtrl.showHomeScreen();
    }

    public void playAgain() {
        if (mainCtrl.getUsernameOriginScreen() == 1) {
            mainCtrl.consecutiveSinglePlayerGame(mainCtrl.getCurrentUsername());
        } else {
            mainCtrl.showWaitingRoom();
        }
    }

    public void setScoreLabel(int score){
        scoreLabel.setText("Your score is " + score);
    }
}
