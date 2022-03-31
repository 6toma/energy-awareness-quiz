package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.questions.ComparativeQuestion;
import commons.questions.EqualityQuestion;
import commons.questions.MCQuestion;
import commons.questions.Question;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class ComparativeQuestionScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private boolean multiplayer;

    /**
     * Integer to set the question mode
     * 0 - ComparativeQuestion
     * 1 - MCQuestion
     * 2 - EqualityQuestion
     */
    private int questionMode = 0;

    private ComparativeQuestion question;
    private MCQuestion mcQuestion;
    private EqualityQuestion equalityQuestion;

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

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private ImageView image3;

    @FXML
    private VBox questionBox1;

    @FXML
    private VBox questionBox2;

    @FXML
    private VBox questionBox3;

    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public ComparativeQuestionScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
     * Runs when answer option 1 is clicked
     * Sets color of the clicked button to yellow, others to default
     */
    public void answer1Clicked(){
        checkAnswer(0);
        answer1.setStyle("-fx-background-color: #fccf03;");
        answer2.setStyle("");
        answer3.setStyle("");
    }

    /**
     * Runs when answer option 2 is clicked
     * Sets color of the clicked button to yellow, others to default
     */
    public void answer2Clicked(){
        checkAnswer(1);
        answer1.setStyle("");
        answer2.setStyle("-fx-background-color: #fccf03;");
        answer3.setStyle("");
    }

    /**
     * Runs when answer option 3 is clicked
     * Sets color of the clicked button to yellow, others to default
     */
    public void answer3Clicked(){
        checkAnswer(2);
        answer1.setStyle("");
        answer2.setStyle("");
        answer3.setStyle("-fx-background-color: #fccf03;");
    }

    private void checkAnswer(int answer){
        int correctAnswer = -1;
        if(questionMode == 0){
            correctAnswer = question.getCorrect_answer();
        } else if(questionMode == 1){
            correctAnswer = mcQuestion.getCorrect_answer();
        } else if(questionMode == 2){
            correctAnswer = equalityQuestion.getCorrect_answer();
        }

        if(answer != correctAnswer){
            timeWhenAnswered = -1;
        } else {
            timeWhenAnswered = (int) (progressBar.getProgress() * questionTime);
        }
    }

    /**
     * Sets the question object for this screen
     * Also sets the question label and answer button texts
     * @param question
     */
    public void setQuestion(Question question) {
        if(question instanceof ComparativeQuestion){
            this.question = (ComparativeQuestion) question;
            this.questionMode = 0;
            setQuestionText();
            setAnswerTexts();
            setImages();
        } else if(question instanceof MCQuestion){
            this.mcQuestion = (MCQuestion) question;
            this.questionMode = 1;
            setMCQuestionText();
            setMCAnswerTexts();
            setMCImages();
        } else if(question instanceof EqualityQuestion){
            this.equalityQuestion = (EqualityQuestion) question;
            this.questionMode = 2;
            setEqualityText();
            setEqualityAnswerTexts();
            setEqualityImages();
        }

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

    /**
     * Sets the images to the ones stored in the activities.
     * Also sets the images to be the same width as the question
     */
    private void setImages(){
        // Adding the images to a list to avoid duplicate code
        List<ImageView> images = List.of(image1, image2, image3);
        // This loops through every activity, gets the image and sets the image in the UI
        for(int i = 0; i < question.getActivities().size(); i++){
            if(question.getActivities().get(i).getImage() != null){
                InputStream inputStream = new ByteArrayInputStream(question.getActivities().get(i).getImage());
                if(inputStream != null){
                    images.get(i).setImage(new Image(inputStream));
                }
            }
        }
        // It's dumb that we have to set the images to be the width of the vbox here
        // A true javafx moment
        image1.fitWidthProperty().bind(questionBox1.widthProperty());
        image2.fitWidthProperty().bind(questionBox2.widthProperty());
        image3.fitWidthProperty().bind(questionBox3.widthProperty());
    }

    private void setMCQuestionText(){
        String questionText = "How much energy does " + this.mcQuestion.getActivity().getTitle() + " use?";
        this.questionLabel.setText(questionText);
    }

    private void setMCAnswerTexts(){
        answer1.setText(this.mcQuestion.getOptions().get(0) + " Wh");
        answer2.setText(this.mcQuestion.getOptions().get(1) + " Wh");
        answer3.setText(this.mcQuestion.getOptions().get(2) + " Wh");
    }

    /**
     * Sets the image to the one stored in the activity.
     * Also sets the image to be the same width as the question
     */
    private void setMCImages(){
        // This loops through every activity, gets the image and sets the image in the UI
        if(mcQuestion.getActivity().getImage() != null){
            InputStream inputStream = new ByteArrayInputStream(mcQuestion.getActivity().getImage());
            if(inputStream != null){
                image2.setImage(new Image(inputStream));
            }
        }
        // set the images to be the width of the vbox
        image2.fitWidthProperty().bind(questionBox2.widthProperty());
    }

    private void setEqualityText(){
        String questionText = "Instead of " + equalityQuestion.getChosen().getTitle() + " you could ...";
        this.questionLabel.setText(questionText);
    }

    private void setEqualityAnswerTexts(){
        answer1.setText(this.equalityQuestion.getActivities().get(0).getTitle());
        answer2.setText(this.equalityQuestion.getActivities().get(1).getTitle());
        answer3.setText(this.equalityQuestion.getActivities().get(2).getTitle());
    }

    /**
     * Sets the images to the ones stored in the activities.
     * Also sets the images to be the same width as the question
     */
    private void setEqualityImages(){
        // Adding the images to a list to avoid duplicate code
        List<ImageView> images = List.of(image1, image2, image3);
        // This loops through every activity, gets the image and sets the image in the UI
        for(int i = 0; i < equalityQuestion.getActivities().size(); i++){
            if(equalityQuestion.getActivities().get(i).getImage() != null){
                InputStream inputStream = new ByteArrayInputStream(equalityQuestion.getActivities().get(i).getImage());
                if(inputStream != null){
                    images.get(i).setImage(new Image(inputStream));
                }
            }
        }
        // It's dumb that we have to set the images to be the width of the vbox here
        // A true javafx moment
        image1.fitWidthProperty().bind(questionBox1.widthProperty());
        image2.fitWidthProperty().bind(questionBox2.widthProperty());
        image3.fitWidthProperty().bind(questionBox3.widthProperty());
    }

    /**
     * Exits the screen. Goes back to the home screen
     */
    public void exit() {
        mainCtrl.showHomeScreen();
        stopTimers();
        resetComparativeQuestionScreen();
    }

    /**
     * Uses a Timeline object to create the progress bar and timer
     * Timeline is like animation, it uses KeyFrame objects which set at which point in what should the scene look like
     * Keyframes can also run code by adding a lambda function in them
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

    private void showAnswers(){

        // This creates another timeline for the timer for the answerTime. See countdown() for a more in-depth breakdown
        KeyFrame start = new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0));
        KeyFrame aEnd = new KeyFrame(Duration.seconds(answerTime), e -> {
            if(multiplayer){
                reset();
            } else {
                endQuestion(); // end the question when the animation is done
            }
        }, new KeyValue(progressBar.progressProperty(), 1));
        answerTimer = new Timeline(start, aEnd);
        answerTimer.setCycleCount(1);
        answerTimer.play();

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

        int correctAnswer = -1;
        if(questionMode == 0){
            correctAnswer = question.getCorrect_answer();
        } else if(questionMode == 1){
            correctAnswer = mcQuestion.getCorrect_answer();
        } else if(questionMode == 2){
            correctAnswer = equalityQuestion.getCorrect_answer();
        }

        if (multiplayer) {
            sendScoreMultiplayer(timeWhenAnswered);
        }else {
            pointsGainedForQuestion = mainCtrl.getSinglePlayerGame().addPoints(timeWhenAnswered, 1.0);
        }
        // highlight correct answer
        if(correctAnswer == 0){
            answer1.setStyle("-fx-background-color: #00ff00;");
        } else if(correctAnswer == 1){
            answer2.setStyle("-fx-background-color: #00ff00;");
        } else {
            answer3.setStyle("-fx-background-color: #00ff00;");
        }

        // make it so that answers also show the respective consumptions
        if(questionMode == 0){
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
        } else if(questionMode == 2){
            answer1.setText(
                this.equalityQuestion.getActivities().get(0).getTitle()
                    + " - " + this.equalityQuestion.getActivities().get(0).getConsumption_in_wh()
                    + " Wh");
            answer2.setText(
                this.equalityQuestion.getActivities().get(1).getTitle()
                    + " - " + this.equalityQuestion.getActivities().get(1).getConsumption_in_wh()
                    + " Wh");
            answer3.setText(
                this.equalityQuestion.getActivities().get(2).getTitle()
                    + " - " + this.equalityQuestion.getActivities().get(2).getConsumption_in_wh()
                    + " Wh");
        }
    }

    /**
     * updates the score of the player in the server
     * @param time time of answer
     */
    public void sendScoreMultiplayer(int time){
        double guessQuestionRate=1.0;
        if(time == -1){
            mainCtrl.getPlayer().resetStreak();
        } else {
            mainCtrl.getPlayer().incrementStreak();
            int currentScore = mainCtrl.getPlayer().getScore();
            long points = Math.round(((100.0 +mainCtrl.getPlayer().getStreak()) / 100.0) * (1050 - 5 * time));
            int pointsToBeAdded = (int) Math.round(guessQuestionRate * points);
            mainCtrl.getPlayer().setScore(currentScore + pointsToBeAdded);
        }
        server.postScore(mainCtrl.getPlayer());
    }


    // reset attributes to default
    private void reset(){
        timeWhenAnswered = -1;
        answer1.setStyle("");
        answer2.setStyle("");
        answer3.setStyle("");

        // re-enable answers
        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);

        // re-enable jokers
        resetJokers();

        // reset images
        image1.setImage(null);
        image2.setImage(null);
        image3.setImage(null);
        this.questionMode = 0;
    }

    private void endQuestion(){
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

    /**
     * Resets the comparative question screen
     */
    public void resetComparativeQuestionScreen() {
        reset();
        resetJokers();
        //chat/emoji will possibly have to be included as well
    }

    /**
     * Adds question to game maxquestions (because joker skips a question)
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

    /**
     * Method which stops the timeline animations.
     * This needs to be done when leaving the screen before the animation is finished,
     * otherwise the code at the end will still be run
     */
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
