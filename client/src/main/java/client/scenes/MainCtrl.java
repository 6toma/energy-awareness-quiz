package client.scenes;

import client.SinglePlayerGame;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.questions.ComparativeQuestion;
import commons.questions.EqualityQuestion;
import commons.questions.EstimationQuestion;
import commons.questions.MCQuestion;
import commons.questions.Question;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import lombok.Getter;

public class MainCtrl {

    @Getter
    private final ServerUtils server;

    @Getter
    private Stage primaryStage;

    private HomeScreenCtrl homeScreenCtrl;
    @Getter
    private Parent homeScreenParent;

    private WaitingRoomCtrl waitingRoomCtrl;
    private Parent waitingRoomParent;

    private LoadingScreenCtrl loadingScreenCtrl;
    private Parent loadingScreenParent;

    private ComparativeQuestionScreenCtrl comparativeQuestionScreenCtrl;
    private Parent comparativeQuestionScreenParent;

    private UsernameScreenCtrl usernameScreenCtrl;
    private Parent usernameScreenParent;

    private EndScreenCtrl endScreenCtrl;
    private Parent endScreenParent;

    private HelpScreenCtrl helpScreenCtrl;
    private Parent helpScreenParent;

    private ScoreChangeScreenCtrl scoreChangeScreenCtrl;
    private Parent scoreChangeScreenParent;

    private EstimationQuestionCtrl estimationScreenCtrl;
    private Parent estimationQuestionParent;

    private SettingsScreenCtrl settingsScreenCtrl;
    private Parent settingsScreenParent;


    // single player variables
    @Getter
    private SinglePlayerGame singlePlayerGame;
    private int singlePlayerGameQuestions = 5;

    /**
     * Creates a new MainCtrl with server
     *
     * @param server ServerUtils object
     */
    @Inject
    public MainCtrl(ServerUtils server) {
        this.server = server;
    }

    /**
     * Initializes the screen
     *
     * @param primaryStage              The primary stage to use (window)
     * @param homeScreen                Screens that main controller can communicate with
     * @param waitingRoom
     * @param loadingScreen
     * @param comparativeQuestionScreen
     * @param usernameScreen
     * @param endScreen
     * @param helpScreen
     * @param scoreChangeScreen
     */
    public void initialize(
            Stage primaryStage,
            Pair<HomeScreenCtrl, Parent> homeScreen,
            Pair<WaitingRoomCtrl, Parent> waitingRoom,
            Pair<LoadingScreenCtrl, Parent> loadingScreen,
            Pair<ComparativeQuestionScreenCtrl, Parent> comparativeQuestionScreen,
            Pair<UsernameScreenCtrl, Parent> usernameScreen,
            Pair<EndScreenCtrl, Parent> endScreen,
            Pair<HelpScreenCtrl, Parent> helpScreen,
            Pair<ScoreChangeScreenCtrl, Parent> scoreChangeScreen,
            Pair<SettingsScreenCtrl, Parent> settingsScreen,
            Pair<EstimationQuestionCtrl, Parent> estimationQuestion
    ) {
        this.primaryStage = primaryStage;

        this.homeScreenCtrl = homeScreen.getKey();
        this.homeScreenParent = homeScreen.getValue();

        this.waitingRoomCtrl = waitingRoom.getKey();
        this.waitingRoomParent = waitingRoom.getValue();

        this.loadingScreenCtrl = loadingScreen.getKey();
        this.loadingScreenParent = loadingScreen.getValue();

        this.comparativeQuestionScreenCtrl = comparativeQuestionScreen.getKey();
        this.comparativeQuestionScreenParent = comparativeQuestionScreen.getValue();

        this.usernameScreenCtrl = usernameScreen.getKey();
        this.usernameScreenParent = usernameScreen.getValue();

        this.endScreenCtrl = endScreen.getKey();
        this.endScreenParent = endScreen.getValue();

        this.helpScreenCtrl = helpScreen.getKey();
        this.helpScreenParent = helpScreen.getValue();

        this.scoreChangeScreenCtrl = scoreChangeScreen.getKey();
        this.scoreChangeScreenParent = scoreChangeScreen.getValue();


        this.estimationScreenCtrl = estimationQuestion.getKey();
        this.estimationQuestionParent = estimationQuestion.getValue();

        this.settingsScreenCtrl = settingsScreen.getKey();
        this.settingsScreenParent = settingsScreen.getValue();


        // TODO: uncomment to disable the fullscreen popup
        //primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(new Scene(homeScreenParent));
        primaryStage.show();
        primaryStage.setFullScreen(true);
        checkDarkMode();

        // Sets proper exit code to window close request
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                waitingRoomCtrl.stop();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /**
     * method for showing the home screen
     */
    public void showHomeScreen() {
        primaryStage.getScene().setRoot(homeScreenParent);
        checkDarkMode();
    }

    /**
     * method for showing the waiting room
     */
    public void showWaitingRoom() {
        primaryStage.getScene().setRoot(waitingRoomParent);
        waitingRoomCtrl.startListening();
        checkDarkMode();
    }

    /**
     * method for showing the settings screen
     */
    public void showSettingsScreen() {
        primaryStage.getScene().setRoot(settingsScreenParent);
        checkDarkMode();
    }

    /**
     * method for showing the laoding screen
     */
    public void showLoadingScreen() {
        primaryStage.getScene().setRoot(loadingScreenParent);
        checkDarkMode();
        loadingScreenCtrl.countdown();
    }

    /**
     * method for showing the username screen
     */
    public void showUsernameScreen() {
        primaryStage.getScene().setRoot(usernameScreenParent);
        checkDarkMode();
    }

    /**
     * method for showing the comparative question
     */
    public void showComparativeQuestionScreen() {
        primaryStage.getScene().setRoot(comparativeQuestionScreenParent);
        checkDarkMode();
        comparativeQuestionScreenCtrl.countdown();
    }

    /**
     * method for showing an Estimation question
     */
    public void showEstimationQuestionScreen() {
        primaryStage.getScene().setRoot(estimationQuestionParent);
        checkDarkMode();
        estimationScreenCtrl.countdown();
    }


    /**
     * method for showing the end screen
     */
    public void showEndScreen() {
        primaryStage.getScene().setRoot(endScreenParent);
        checkDarkMode();
    }

    /**
     * method for showing the help screen
     */
    public void showHelpScreen() {
        ((StackPane) primaryStage.getScene().getRoot()).getChildren().add(helpScreenParent);
        checkDarkMode();
    }

    /**
     * method for hiding the help screen
     */
    public void hideHelpScreen() {
        ((StackPane) primaryStage.getScene().getRoot()).getChildren().remove(helpScreenParent);
        checkDarkMode();
    }

    /**
     * method for showing the score change screen
     */
    public void showScoreChangeScreen(int pointsGained) {
        primaryStage.getScene().setRoot(scoreChangeScreenParent);
        checkDarkMode();
        showScore(pointsGained);
        scoreChangeScreenCtrl.countdown();
    }


    /**
     * method for changing mode to opposite colour
     */
    public void checkDarkMode() {
        if (settingsScreenCtrl.getDarkMode()) {
            primaryStage.getScene().getRoot().setBlendMode(BlendMode.DIFFERENCE);
        } else {
            primaryStage.getScene().getRoot().setBlendMode(null);
        }
    }

    /**
     * Gets the origin of usernamescreen
     *
     * @return 1 - Singleplayer, 2 - Multiplayer
     */
    public int getUsernameOriginScreen() {
        return homeScreenCtrl.getUsernameOriginScreen();
    }

    /**
     * Sets the usernameOriginScreen
     *
     * @param usernameOriginScreen value
     *                             1 - Singleplayer
     *                             2 - Multiplayer
     */
    public void setUsernameOriginScreen(int usernameOriginScreen) {
        homeScreenCtrl.setUsernameOriginScreen(usernameOriginScreen);
    }

    /**
     * Resets the username text in usernamescreen
     */
    public void resetUserText() {
        usernameScreenCtrl.resetUserText();
    }

    /**
     * Checks for connection
     * Creates a new game with some number of questions
     */
    public void newSinglePlayerGame() {
        try {
            Question question = server.getRandomQuestion();
            singlePlayerGame = new SinglePlayerGame(singlePlayerGameQuestions);
            singlePlayerGame.addQuestion(question);

            setUsernameOriginScreen(1);
            showUsernameScreen();
        } catch(Exception e){
            e.printStackTrace();
            showPopup("Connection failed");
        }
    }

    /**
     * Similar to newSinglePlayerGame(), but requires a username
     *
     * @param username The username, used in the previous game
     */
    public void consecutiveSinglePlayerGame(String username) {
        try {
            Question question = server.getRandomQuestion();

            singlePlayerGame = new SinglePlayerGame(singlePlayerGameQuestions, username);
            singlePlayerGame.addQuestion(question);

            //skipping over the part where we ask for username
            showLoadingScreen();
        } catch(Exception e){
            e.printStackTrace();
            showPopup("Connection failed");
        }

    }

    /**
     * Shows the correct question screen based on the next question
     * <p>
     * Shows the end screen if next question isn't defined
     */
    public void nextQuestionScreen() {
        // check if there's a next question to show
        if (singlePlayerGame != null
                && singlePlayerGame.getQuestions().size() > 0
                && singlePlayerGame.getQuestionNumber() <= singlePlayerGame.getMaxQuestions()
                + comparativeQuestionScreenCtrl.jokerAdditionalQuestion()) {

            try {
                // get next question from the server
                Question newQuestion = server.getRandomQuestion();
                // loop until new question is not already in the list
                while (!singlePlayerGame.addQuestion(newQuestion)) {
                    newQuestion = server.getRandomQuestion();
                }

                Question question = singlePlayerGame.getQuestions().get(singlePlayerGame.getQuestionNumber() - 1);
                // check the question type
                if (question instanceof ComparativeQuestion
                    || question instanceof MCQuestion
                    || question instanceof EqualityQuestion) {

                    showComparativeQuestionScreen();
                    comparativeQuestionScreenCtrl.setQuestion(question);
                } else if (question instanceof EstimationQuestion) {
                    showEstimationQuestionScreen();
                    estimationScreenCtrl.setQuestion((EstimationQuestion) question);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showPopup("Connection failed");
                showHomeScreen();
            }

        } else { // if no question to show display end screen
            endSinglePlayerGame();
        }
    }


    /**
     * Called to end the single player game
     * Shows the end screen and sends score to the server
     */
    public void endSinglePlayerGame() {
        //show End screen with score
        endScreenCtrl.setScoreLabel(singlePlayerGame.getPlayer().getScore());
        showEndScreen();

        //reset Question screen to prepare it for a new game
        comparativeQuestionScreenCtrl.resetComparativeQuestionScreen();
        estimationScreenCtrl.resetEstimationQuestion();

        //store player's end score
        try {
            server.postPlayer(singlePlayerGame.getPlayer());
        } catch (Exception e) {
            e.printStackTrace();
            showPopup("Connection failed");
        }
    }

    /**
     * Shows the current score on the score change screen
     *
     * @param pointsGained number of points to be added to the score
     */
    public void showScore(int pointsGained) {
        int gained = pointsGained;
        int total = singlePlayerGame.getPlayer().getScore();
        int streak = singlePlayerGame.getPlayer().getStreak();
        scoreChangeScreenCtrl.setScoreLabels(gained, total, streak);
    }

    /**
     * Gets the username
     *
     * @return Username of current player of singlePlayerGame
     */
    public String getCurrentUsername() {
        return this.singlePlayerGame.getPlayer().getName();
    }

    /**
     * Gets the server url from the settings screen
     *
     * @return
     */
    public String getServerURL() {
        return this.settingsScreenCtrl.getServerURL();
    }

    /**
     * Shows an error popup message
     * @param message to be shown on the popup
     */
    public void showPopup(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

