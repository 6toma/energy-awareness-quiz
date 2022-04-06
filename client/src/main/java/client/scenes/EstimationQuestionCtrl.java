package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.questions.EstimationQuestion;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private double additionalPoints = 1.0; // if joker "double points" is used, it is set to 2.0

    // Timeline objects used for animating the progressbar
    // Global objects because they need to be accessed from different methods
    private Timeline questionTimer;
    private Timeline answerTimer;
    private boolean multiplayer;


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

    @FXML
    private Label jokerMessage;

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
     * sets multiplayer flag
     * @param v multiplayer bool
     */
    public void setMultiplayer(boolean v){
        this.multiplayer = v;
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
     * Sets the question object for this screen
     * Also sets the question label and answer text field
     * @param question
     */
    public void setQuestion(EstimationQuestion question) {
        this.question = question;
        setQuestionText();
        // Sets a formatter for the input field to only accept numbers
        answerField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("[0-9]*") ? change : null));
        setImage();
        setJokers();
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
            if (multiplayer) {
                reset();
            } else {
                endQuestion(); // end the question when the animation is done
            }
        }, new KeyValue(progressBar.progressProperty(), 1));
        answerTimer = new Timeline(start, aEnd);
        answerTimer.setCycleCount(1);
        answerTimer.play();

        answerField.setDisable(true);
        // disable joker buttons, so they can't be clicked while
        // answers are being shown
        joker1.setMouseTransparent(true);
        joker2.setMouseTransparent(true);
        joker3.setMouseTransparent(true);

        questionLabel.setText(questionLabel.getText() + " - " + question.getActivity().getConsumption_in_wh() + " Wh");

        if (multiplayer) {
            mainCtrl.addScoreMultiplayer(timeWhenAnswered, guessAccuracy);
        } else {
            pointsGainedForQuestion = mainCtrl.getSinglePlayerGame().addPoints(timeWhenAnswered, additionalPoints * guessAccuracy);
            additionalPoints = 1.0;
        }
    }

    private void reset() {
        timeWhenAnswered = -1;

        // re-enable jokers
        setJokers();

        // reset answerField text and status
        answerField.setText("");
        answerField.setDisable(false);

        jokerMessage.setText("");
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
        if(!multiplayer) {
            joker1.setDisable(true);
            mainCtrl.getSinglePlayerGame().useJokerAdditionalQuestion();

            stopTimers();
            /* even if the correct answer was selected before the question was changed, 0 points will be added
             * the method addPoints() is used just to increment the number of the current question in the list
             * streak is reset to 0
             */
            pointsGainedForQuestion = mainCtrl.getSinglePlayerGame().addPoints(-1, 0.0);
            endQuestion();
        }
    }

    @FXML
    private void joker2() {
        if(!multiplayer) {
            // if there is no answer input, don't use joker
            if (answerField.getText().equals("")) {
                jokerMessage.setText("Input an answer to use this joker!");
                return;
            }

            joker2.setDisable(true); // disable button
            mainCtrl.getSinglePlayerGame().useJokerRemoveOneAnswer();

            /* calculate the points the player would win for this question
             * the same way they are calculated in addPoints(), but without actually adding them
             */
            int pointsToBeAdded = (int) Math.round(guessAccuracy * additionalPoints * mainCtrl.getSinglePlayerGame().getPointsToBeAdded(timeWhenAnswered));

            if (pointsToBeAdded > 0) {
                jokerMessage.setText("Close enough! You will get some points for this answer.");
            } else {
                jokerMessage.setText("You guess is too far from the actual answer! Try changing it so you can get some points for this question.");
            }
        }
    }

    @FXML
    private void joker3() {
        if(!multiplayer) {
            joker3.setDisable(true); // disable button
            mainCtrl.getSinglePlayerGame().useJokerDoublePoints();

            additionalPoints = 2.0; // points will be double only for the current question
        }
    }

    /**
     * Reset the states of the jokers. Enable all jokers and set their usage to false.
     */
    private void resetJokers() {
        joker1.setDisable(false);
        joker2.setDisable(false);
        joker3.setDisable(false);
        joker1.setMouseTransparent(false);
        joker2.setMouseTransparent(false);
        joker3.setMouseTransparent(false);
    }

    /**
     * Reset an estimation question
     */
    public void resetEstimationQuestion() {
        stopTimers();
        reset();
        resetJokers();
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

    /**
     * Updates the disabling of the buttons used for jokers, in case
     * a joker has been used on another screen.
     * Resets the mouse-transparency, used when answers are being shown
     */
    private void setJokers() {
        if(!multiplayer) {
            if (mainCtrl.getSinglePlayerGame().jokerAdditionalQuestionIsUsed()) {
                joker1.setDisable(true);
            } else {
                joker1.setMouseTransparent(false);
            }

            if (mainCtrl.getSinglePlayerGame().jokerRemoveOneAnswerIsUsed()) {
                joker2.setDisable(true);
            } else {
                joker2.setMouseTransparent(false);
            }

            if (mainCtrl.getSinglePlayerGame().jokerDoublePointsIsUsed()) {
                joker3.setDisable(true);
            } else {
                joker3.setMouseTransparent(false);
            }
        }
    }

    /**
     * Add tooltips for the jokers
     */
    public void addTooltips() {
        Tooltip skipQuestion = new Tooltip();
        skipQuestion.setText("Skip this question!");
        skipQuestion.setShowDelay(Duration.ZERO);
        joker1.setTooltip(skipQuestion);

        Tooltip cutAnswer = new Tooltip();
        cutAnswer.setText("Reveal a wrong answer!");
        cutAnswer.setShowDelay(Duration.ZERO);
        joker2.setTooltip(cutAnswer);

        Tooltip doublePoints = new Tooltip();
        doublePoints.setText("Double score intake for this question!");
        doublePoints.setShowDelay(Duration.ZERO);
        joker3.setTooltip(doublePoints);
    }

}
