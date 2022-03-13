package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

public class ScoreChangeScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label scoreGained;
    @FXML
    private Label scoreTotal;
    @FXML
    private Label scoreStreak;
    @FXML
    private Button leave;

    private Timer timer = new Timer();

    @Inject
    public ScoreChangeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setScoreLabels(int gained, int total, int streak ){
        scoreGained.setText("+" + gained);
        scoreTotal.setText("Score: " + total);
        scoreStreak.setText("Steak: " + streak);
    }

    public void exit() {
        mainCtrl.showHomeScreen();
        timer.cancel();
        timer = new Timer();
    }

    public void countdown() {

        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mainCtrl.nextQuestionScreen();
                    }
                });
            }
        };

        timer.schedule(task, 3000);
    }


}
