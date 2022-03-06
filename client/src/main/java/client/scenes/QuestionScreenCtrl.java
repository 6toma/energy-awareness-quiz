package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class QuestionScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Timer timer = new Timer();

    @FXML
    private Button exit;

    @FXML
    private ProgressBar progressBar;

    @Inject
    public QuestionScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void exit() {
        mainCtrl.showHomeScreen();
        timer.cancel();
        timer = new Timer();
    }

    public void countdown() {

        TimerTask task = new TimerTask() {
            double progress = 0.0;

            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                        progress += 0.05;
                    }
                });

                if(progress >= 1.0) {
                    progressBar.setProgress(0.0);
                    progress = 0.0;
                    cancel();
                }
            }
        };

        timer.schedule(task, 0, 1000);
    }
}
