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

    // Strings used to construct the question text
    private String questionTextStart = "Which activity uses the ";
    private String questionTextIsMost = "most";
    private String questionTextNotMost = "least";
    private String questionTextEnd = " energy?";

    // for how long to show question and answer
    private double questionTime = 5.0;
    private double answerTime = 4.0;

    private int timeWhenAnswered = -1;
    private int currentTime = (int) questionTime;

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
        reset();
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
            timeWhenAnswered = currentTime;
        }
    }

    private void showAnswers(){
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
        currentTime = (int) questionTime;
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
}
