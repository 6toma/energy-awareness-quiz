package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.ComparativeQuestion;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class ComparativeQuestionScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ComparativeQuestion question;

    // Strings used to construct the question text
    private String questionTextStart = "Which activity uses the ";
    private String questionTextIsMost = "most";
    private String questionTextNotMost = "least";
    private String questionTextEnd = " energy?";

    // for how long to show question and answer
    private double questionTime = 15.0;
    private double answerTime = 4.0;

    // FPS of the progress bar
    private int FPS = 60;

    private int timeWhenAnswered = -1;

    private boolean joker1Used = false;
    private boolean joker2Used = false;
    private boolean joker3Used = false;

    private Timeline questionTimer;
    private Timeline answerTimer;

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
        stopTimers();
        resetComparativeQuestionScreen();
    }

    public void countdown() {

        KeyFrame start = new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0));
        KeyFrame qEnd = new KeyFrame(Duration.seconds(questionTime), e -> {
            showAnswers();
        }, new KeyValue(progressBar.progressProperty(), 1));

        questionTimer = new Timeline(start, qEnd);
        questionTimer.setCycleCount(1);

        questionTimer.play();
    }

    public void setQuestion(ComparativeQuestion question) {
        this.question = question;
        setQuestionText();
        setAnswerTexts();
    }

    private void setQuestionText(){
        String questionText = questionTextStart;
        if(this.question.isMost()){
            questionText += questionTextIsMost;
        } else {
            questionText += questionTextNotMost;
        }
        this.questionLabel.setText(questionText + questionTextEnd);
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
            timeWhenAnswered = (int) (progressBar.getProgress() * questionTime);
        }
    }

    private void showAnswers(){
        KeyFrame start = new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0));
        KeyFrame aEnd = new KeyFrame(Duration.seconds(answerTime), e -> {
            endQuestion();
        }, new KeyValue(progressBar.progressProperty(), 1));
        answerTimer = new Timeline(start, aEnd);
        answerTimer.setCycleCount(1);
        answerTimer.play();

        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);

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
    }

    // reset attributes to default
    private void reset(){
        timeWhenAnswered = -1;
        answer1.setStyle("");
        answer2.setStyle("");
        answer3.setStyle("");
        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);
    }

    private void endQuestion(){
        reset();
        mainCtrl.nextQuestionScreen();
    }

    @FXML
    private void joker1() {
        joker1.setDisable(true);
        joker1Used = true;

        stopTimers();
        //even if the correct answer was selected before the question was changed, points won't be added
        timeWhenAnswered = -1;
        //doesn't add points, but is used to increment the number of the current guestion in the list
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

    private void stopTimers(){
        if(questionTimer != null){
            questionTimer.stop();
            questionTimer = null;
        }
        if(answerTimer != null){
            answerTimer.stop();
            answerTimer = null;
        }
    }
}
