package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.EstimationQuestion;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class EstimationQuestionCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private EstimationQuestion question;

    // for how long to show question and answer
    private double questionTime = 15.0;
    private double answerTime = 4.0;

    private int timeWhenAnswered = -1;
    private int currentTime = (int) questionTime;
    private int pointsGainedForQuestion = 0;

    private boolean joker1Used = false;
    private boolean joker2Used = false;
    private boolean joker3Used = false;

    // Timeline objects used for animating the progressbar
    // Global objects because they need to be accessed from different methods
    private Timeline questionTimer;
    private Timeline answerTimer;


    @FXML
    private Label questionLabel;

    @FXML
    private Button exit;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button joker1;

    @FXML
    private Button joker2;

    @FXML
    private Button joker3;


    @Inject
    public EstimationQuestionCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void exit() {
        mainCtrl.showHomeScreen();
        stopTimers();
        resetEstimationQuestion();
    }

    public void countdown() {

        // set the progressbar value to be 0 at the beginning of the animation
        KeyFrame start = new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0));

        // set the keyframe at the end of the animation
        KeyFrame qEnd = new KeyFrame(Duration.seconds(questionTime), e -> { // whatever is in e -> {} will be run when this keyframe is reached
            showAnswers(); // show answers when the animation is done
        }, new KeyValue(progressBar.progressProperty(), 1)); // set the progressbar value to be 1

        // initialize the timeline with the 2 keyframes
        questionTimer = new Timeline(start, qEnd);
        // set timeline to only run once (can also be made to loop indefinitely)
        questionTimer.setCycleCount(1);

        // starts the timeline
        questionTimer.play();
    }

    public void setQuestion(EstimationQuestion question) {
        this.question = question;
        setQuestionText();
    }

    private void setQuestionText() {
        String questionText = "How much energy does " + this.question.getActivities().get(0).getTitle() + " use?";
        this.questionLabel.setText(questionText);
    }

    public void checkAnswer(Long answer) {
        Long correctAnswer = question.getCorrect_answer();
        if (answer != correctAnswer) {
            timeWhenAnswered = -1;
        } else {
            timeWhenAnswered = (int) (progressBar.getProgress() * questionTime);
        }
    }

    private void showAnswers() {

        // This creates another timeline for the timer for the answerTime. See countdown() for a more in-depth breakdown
        KeyFrame start = new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0));
        KeyFrame aEnd = new KeyFrame(Duration.seconds(answerTime), e -> {
            endQuestion(); // end the question when the animation is done
        }, new KeyValue(progressBar.progressProperty(), 1));
        answerTimer = new Timeline(start, aEnd);
        answerTimer.setCycleCount(1);
        answerTimer.play();

        //answer.setDisable(true);
        // disable joker buttons, so they can't be clicked while
        // answers are being shown
        joker1.setDisable(true);
        joker2.setDisable(true);
        joker3.setDisable(true);

        Long correctAnswer = question.getCorrect_answer();

        pointsGainedForQuestion = mainCtrl.getSinglePlayerGame().addPoints(timeWhenAnswered, 1.0);


    }

    private void reset() {
        timeWhenAnswered = -1;

        // re-enable jokers
        joker1.setDisable(false);
        joker2.setDisable(false);
        joker3.setDisable(false);
    }

    private void endQuestion() {
        reset();
        mainCtrl.showScoreChangeScreen(pointsGainedForQuestion);
    }

    @FXML
    private void joker1() {
        joker1.setDisable(true);
        joker1Used = true;

        stopTimers();
        //even if the correct answer was selected before the question was changed, points won't be added
        timeWhenAnswered = -1;
        //doesn't add points, but is used to increment the number of the current question in the list
        mainCtrl.getSinglePlayerGame().addPoints(timeWhenAnswered, 1.0);
        endQuestion();
    }

    @FXML
    private void joker2() {
        //implementation for joker
        joker2.setDisable(true); // disable button
    }

    @FXML
    private void joker3() {
        //implementation for joker
        joker3.setDisable(true); // disable button
    }

    public void resetJokers() {
        joker1.setDisable(false);
        joker2.setDisable(false);
        joker3.setDisable(false);
        joker1Used = false;
        joker2Used = false;
        joker3Used = false;
    }

    public void resetEstimationQuestion() {
        reset();
        resetJokers();
    }

    public int jokerAdditionalQuestion() {
        if (joker1Used) {
            return 1;
        } else {
            return 0;
        }
    }

    private void stopTimers() {
        if (questionTimer != null) {
            questionTimer.stop();
            questionTimer = null;
        }
        if (answerTimer != null) {
            answerTimer.stop();
            answerTimer = null;
        }
    }


}
