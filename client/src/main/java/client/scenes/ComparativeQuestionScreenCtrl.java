package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.ComparativeQuestion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class ComparativeQuestionScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Timer timer = new Timer();

    private ComparativeQuestion question;

    // for how long to show question and answer
    private final double questionTime = 5.0;
    private final double answerTime = 4.0;

    private int timeWhenAnswered = -1;
    private int currentTime = (int) questionTime;

    private boolean joker1Used = false;
    private boolean joker2Used = false;
    private boolean joker3Used = false;

    @FXML
    private Label questionLabel;

    @FXML
    private Button answer1;

    @FXML
    private Button answer2;

    @FXML
    private Button answer3;

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
    public ComparativeQuestionScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void answer1Clicked(){
        checkAnswer(0);
        answer1.setStyle("-fx-background-color: #fccf03;");
        answer2.setStyle("");
        answer3.setStyle("");
    }

    public void answer2Clicked(){
        checkAnswer(1);
        answer1.setStyle("");
        answer2.setStyle("-fx-background-color: #fccf03;");
        answer3.setStyle("");
    }

    public void answer3Clicked(){
        checkAnswer(2);
        answer1.setStyle("");
        answer2.setStyle("");
        answer3.setStyle("-fx-background-color: #fccf03;");
    }

    public void exit() {
        mainCtrl.showHomeScreen();
        timer.cancel();
        timer = new Timer();
        resetComparativeQuestionScreen();
    }

    public void countdown() {

        TimerTask task = new TimerTask() {
            double progress = 0.0;
            double progressTimer = questionTime; // how many seconds should the timer last for

            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        if(progress >= 1.0) {
                            // check if it's the end of the question or end of the answer
                            if(progressTimer == questionTime){
                                progressTimer = answerTime;
                                progressBar.setProgress(0.0);
                                progress = 0.0;
                                showAnswers();
                            } else {
                                endQuestion();
                                cancel();
                            }
                        }

                        progressBar.setProgress(progress);
                        progress += 1 / progressTimer;
                        currentTime -= 1;
                    }
                });
            }
        };

        timer.schedule(task, 0, 1000);
    }

    public void setQuestion(ComparativeQuestion question) {
        this.question = question;
        setQuestionText();
        setAnswerTexts();
    }

    private void setQuestionText(){
        // Strings used to construct the question text
        String mostOrLeast;
        if(this.question.isMost()){
            mostOrLeast = "most";
        } else {
            mostOrLeast = "least";
        }
        String questionText = "Which activity uses the " + mostOrLeast + " amount of energy?";
        this.questionLabel.setText(questionText);
    }

    private void setAnswerTexts(){
        answer1.setText(this.question.getActivities().get(0).getTitle());
        answer2.setText(this.question.getActivities().get(1).getTitle());
        answer3.setText(this.question.getActivities().get(2).getTitle());
    }

    public void checkAnswer(int answer){
        int correctAnswer = question.getCorrect_answer();
        if(answer != correctAnswer){
            timeWhenAnswered = -1;
        } else {
            timeWhenAnswered = currentTime;
        }
    }

    private void showAnswers(){
        // disable answer buttons, so they can't be clicked while
        // answers are being shown
        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);

        // disable joker buttons, so they can't be clicked while
        // answers are being shown
        joker1.setDisable(true);
        joker2.setDisable(true);
        joker3.setDisable(true);

        int correctAnswer = question.getCorrect_answer();

        mainCtrl.getSinglePlayerGame().addPoints(timeWhenAnswered, 1.0);
        // highlight correct answer
        if(correctAnswer == 0){
            answer1.setStyle("-fx-background-color: #00ff00;");
        } else if(correctAnswer == 1){
            answer2.setStyle("-fx-background-color: #00ff00;");
        } else {
            answer3.setStyle("-fx-background-color: #00ff00;");
        }

        // make it so that answers also show the respective consumptions
        answer1.setText(
                this.question.getActivities().get(0).getTitle()
                        + " - " + this.question.getActivities().get(0).getConsumption_in_wh()
                        + " Wh");
        answer2.setText(
                this.question.getActivities().get(1).getTitle()
                        + " - " + this.question.getActivities().get(1).getConsumption_in_wh()
                        + " Wh");
        answer3.setText(
                this.question.getActivities().get(2).getTitle()
                        + " - " + this.question.getActivities().get(2).getConsumption_in_wh()
                        + " Wh");
    }

    // reset attributes to default
    private void reset(){
        timeWhenAnswered = -1;
        currentTime = (int) questionTime;
        answer1.setStyle("");
        answer2.setStyle("");
        answer3.setStyle("");

        // re-enable answers
        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);

        // re-enable jokers
        joker1.setDisable(false);
        joker2.setDisable(false);
        joker3.setDisable(false);
    }

    private void endQuestion(){
        reset();
        mainCtrl.nextQuestionScreen();
    }

    @FXML
    private void joker1() {
        joker1.setDisable(true);
        joker1Used = true;
        timer.cancel();
        timer = new Timer();
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

    /**
     * Enables the use of the jokers again for the next game
     *
     * Intentionally a separate method and not included in reset(),
     * because it is used to reset the 3 answer options after every question, but
     * jokers should remain disabled until the end of the game
     */
    public void resetJokers() {
        joker1.setDisable(false);
        joker2.setDisable(false);
        joker3.setDisable(false);
        joker1Used = false;
        joker2Used = false;
        joker3Used = false;
    }

    public void resetComparativeQuestionScreen() {
        reset();
        resetJokers();
        //chat/emoji will possibly have to be included as well
    }

    /**
     * @return 1 - If the joker "Change current question" is used,
     *         in order to add a question to the maximum number of questions in the game;
     *         0 - Otherwise.
     */
    public int jokerAdditionalQuestion() {
        if(joker1Used) {
            return 1;
        } else {
            return 0;
        }
    }
}
