package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.questions.EstimationQuestion;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Data
public class EstimationQuestionCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private EstimationQuestion question;

    // for how long to show question and answer
    private double questionTime = 15.0;
    private double answerTime = 4.0;

    private int timeWhenAnswered = -1;
    private double guessAccuracy = 1.0;
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
    private TextField answerField;

    @FXML
    private ImageView image;

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

    @FXML
    private HBox questionBox;

    /**
     * Constructor for the Estimation Question Controller
     * @param server
     * @param mainCtrl
     */
    @Inject
    public EstimationQuestionCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Exiting the screen
     */
    public void exit() {
        mainCtrl.showHomeScreen();
        stopTimers();
        resetEstimationQuestion();
    }

    /**
     * Function for creating a countdown and a progress bar
     */
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

    /**
     *
     * @param question
     */
    public void setQuestion(EstimationQuestion question) {
        this.question = question;
        setQuestionText();
        // Sets a formatter for the input field to only accept numbers
        answerField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("[0-9]*") ? change : null));
        setImage();
    }

    /**
     * Set textual representation of the question
     */
    private void setQuestionText() {
        String questionText = "How much energy does " + this.question.getActivity().getTitle() + " use?";
        this.questionLabel.setText(questionText);
    }

    /**
     * Checking the correctness of the answer
     * @param answer
     */
    public void checkAnswer(Long answer) {
        guessAccuracy = (double) answer / question.getActivity().getConsumption_in_wh();
        // convert to accuracy value of < 1. for example 1.04 -> 0.96
        if(guessAccuracy > 1.0){
            guessAccuracy = 1 - 2 * (guessAccuracy - 1);
        }
        // set a lower limit to the guess accuracy
        if(guessAccuracy < 0.2){
            guessAccuracy = 0;
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

        answerField.setDisable(true);
        // disable joker buttons, so they can't be clicked while
        // answers are being shown
        joker1.setDisable(true);
        joker2.setDisable(true);
        joker3.setDisable(true);

        questionLabel.setText(questionLabel.getText() + " - " + question.getActivity().getConsumption_in_wh() + " Wh");

        pointsGainedForQuestion = mainCtrl.getSinglePlayerGame().addPoints(timeWhenAnswered, guessAccuracy);
    }

    private void reset() {
        timeWhenAnswered = -1;

        // re-enable jokers
        joker1.setDisable(false);
        joker2.setDisable(false);
        joker3.setDisable(false);

        // reset answerField text and status
        answerField.setText("");
        answerField.setDisable(false);
    }

    private void endQuestion() {
        reset();
        mainCtrl.showScoreChangeScreen(pointsGainedForQuestion);
    }

    @FXML
    void answerTextChanged(KeyEvent event) {
        // try-catch block needed to check for errors when parsing string
        try {
            Long answer = Long.parseLong(answerField.getText());
            checkAnswer(answer);
        } catch(NumberFormatException e){
        }
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

    /**
     * Reset the states of the jokers. Enable all jokers and set their usage to false.
     */
    public void resetJokers() {
        joker1.setDisable(false);
        joker2.setDisable(false);
        joker3.setDisable(false);
        joker1Used = false;
        joker2Used = false;
        joker3Used = false;
    }

    /**
     * Reset an estimation question
     */
    public void resetEstimationQuestion() {
        reset();
        resetJokers();
    }

    /**
     * Joker for additional question
     * @return
     */
    // TODO
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

    /**
     * Sets the images to the ones stored in the activities.
     * Also sets the images to be the same width as the question
     */
    private void setImage(){
        if(question.getActivity().getImage() != null){
            InputStream inputStream = new ByteArrayInputStream(question.getActivity().getImage());
            if(inputStream != null){
                image.setImage(new Image(inputStream));
            }
        }
        image.fitHeightProperty().bind(questionBox.heightProperty());
    }
}
