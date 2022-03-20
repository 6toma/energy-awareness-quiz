package client.scenes;

import client.SinglePlayerGame;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.ComparativeQuestion;
import commons.Question;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

    // single player variables
    @Getter
    private SinglePlayerGame singlePlayerGame;
    private int singlePlayerGameQuestions = 5;

    @Inject
    public MainCtrl(ServerUtils server) {
        this.server = server;
    }

    // default initializing code
    public void initialize(
            Stage primaryStage,
            Pair<HomeScreenCtrl, Parent> homeScreen,
            Pair<WaitingRoomCtrl, Parent> waitingRoom,
            Pair<LoadingScreenCtrl, Parent> loadingScreen,
            Pair<ComparativeQuestionScreenCtrl, Parent> comparativeQuestionScreen,
            Pair<UsernameScreenCtrl, Parent> usernameScreen,
            Pair<EndScreenCtrl, Parent> endScreen,
            Pair<HelpScreenCtrl, Parent> helpScreen,
            Pair<ScoreChangeScreenCtrl, Parent> scoreChangeScreen
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
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void showHomeScreen() {
        primaryStage.getScene().setRoot(homeScreenParent);
        checkDarkMode();
    }

    public void showWaitingRoom() {
        primaryStage.getScene().setRoot(waitingRoomParent);
        checkDarkMode();
    }

    public void showLoadingScreen() {
        primaryStage.getScene().setRoot(loadingScreenParent);
        checkDarkMode();
        loadingScreenCtrl.countdown();
    }

    public void showUsernameScreen() {
        primaryStage.getScene().setRoot(usernameScreenParent);
        checkDarkMode();
    }

    public void showComparativeQuestionScreen() {
        primaryStage.getScene().setRoot(comparativeQuestionScreenParent);
        checkDarkMode();
        comparativeQuestionScreenCtrl.countdown();
    }

    public void showEndScreen() {
        primaryStage.getScene().setRoot(endScreenParent);
        checkDarkMode();
    }

    public void showHelpScreen() {
        ((StackPane) primaryStage.getScene().getRoot()).getChildren().add(helpScreenParent);
        checkDarkMode();
    }

    public void hideHelpScreen() {
        ((StackPane) primaryStage.getScene().getRoot()).getChildren().remove(helpScreenParent);
        checkDarkMode();
    }

    public void showScoreChangeScreen(int pointsGained){
        primaryStage.getScene().setRoot(scoreChangeScreenParent);
        checkDarkMode();
        showScore(pointsGained);
        scoreChangeScreenCtrl.countdown();
    }

    public void checkDarkMode() {
        if (!homeScreenCtrl.getDarkMode()) {
            primaryStage.getScene().getRoot().setBlendMode(BlendMode.DIFFERENCE);
        } else {
            primaryStage.getScene().getRoot().setBlendMode(null);
        }
    }

    public int getUsernameOriginScreen() {
        return homeScreenCtrl.getUsernameOriginScreen();
    }

    public void setUsernameOriginScreen(int usernameOriginScreen) {
        homeScreenCtrl.setUsernameOriginScreen(usernameOriginScreen);
    }

    public void resetUserText() {
        usernameScreenCtrl.resetUserText();
    }

    /**
     * Checks for connection
     * Creates a new game with some number of questions
     */
    public void newSinglePlayerGame() {
        ComparativeQuestion question = server.getCompQuestion();

        singlePlayerGame = new SinglePlayerGame(singlePlayerGameQuestions);
        singlePlayerGame.addQuestion(question);

        setUsernameOriginScreen(1);
        showUsernameScreen();
    }

    /**
     * Similar to newSinglePlayerGame(), but requires a username
     * @param username The username, used in the previous game
     */
    public void consecutiveSinglePlayerGame(String username) {
        ComparativeQuestion question = server.getCompQuestion();

        singlePlayerGame = new SinglePlayerGame(singlePlayerGameQuestions, username);
        singlePlayerGame.addQuestion(question);

        //skipping over the part where we ask for username
        showLoadingScreen();
    }

    /**
     * Shows the correct question screen based on the next question
     * <p>
     * Shows the end screen if next question isn't defined
     */
    public void nextQuestionScreen() {
        if(singlePlayerGame != null
            && singlePlayerGame.getQuestions().size() > 0
            && singlePlayerGame.getQuestionNumber() <= singlePlayerGame.getMaxQuestions()
                + comparativeQuestionScreenCtrl.jokerAdditionalQuestion()) {

            Question question = singlePlayerGame.getQuestions().get(singlePlayerGame.getQuestionNumber() - 1);

            // check the question type
            if (question instanceof ComparativeQuestion) {
                showComparativeQuestionScreen();
                comparativeQuestionScreenCtrl.setQuestion((ComparativeQuestion) question);
            } // more question types to be added

            // get next question from the server
            try {
                ComparativeQuestion newQuestion = server.getCompQuestion();
                // loop until new question is not already in the list
                while (!singlePlayerGame.addQuestion(newQuestion)) {
                    newQuestion = server.getCompQuestion();
                }
            } catch (Exception e) {
                // TODO: error pop-up
                Alert alert = new Alert(Alert.AlertType.NONE);
                EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        // set alert type
                        alert.setAlertType(Alert.AlertType.ERROR);
                        // set content text
                        alert.setContentText("connection failed");
                        // show the dialog
                        alert.show();
                    }
                };
            }

        } else {
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

        //store player's end score
        try{
            server.postPlayer(singlePlayerGame.getPlayer());
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.NONE);
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    // set alert type
                    alert.setAlertType(Alert.AlertType.ERROR);
                    // set content text
                    alert.setContentText("connection failed");
                    // show the dialog
                    alert.show();
                }
            };
        }
    }

    public void showScore(int pointsGained) {
        int gained = pointsGained;
        int total = singlePlayerGame.getPlayer().getScore();
        int streak = singlePlayerGame.getStreak();
        scoreChangeScreenCtrl.setScoreLabels(gained, total, streak);
    }

    public String getCurrentUsername() {
        return this.singlePlayerGame.getPlayer().getName();
    }
}

