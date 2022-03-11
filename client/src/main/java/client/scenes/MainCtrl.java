package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private HomeScreenCtrl homeScreenCtrl;
    private Parent homeScreenParent;

    private WaitingRoomCtrl waitingRoomCtrl;
    private Parent waitingRoomParent;

    private LoadingScreenCtrl loadingScreenCtrl;
    private Parent loadingScreenParent;

    private QuestionScreenCtrl questionScreenCtrl;
    private Parent questionScreenParent;

    private UsernameScreenCtrl usernameScreenCtrl;
    private Parent usernameScreenParent;

    private EndScreenCtrl endScreenCtrl;
    private Parent endScreenParent;

    private HelpScreenCtrl helpScreenCtrl;
    private Parent helpScreenParent;

    // default initializing code
    public void initialize(
            Stage primaryStage,
            Pair<HomeScreenCtrl, Parent> homeScreen,
            Pair<WaitingRoomCtrl, Parent> waitingRoom,
            Pair<LoadingScreenCtrl, Parent> loadingScreen,
            Pair<QuestionScreenCtrl, Parent> questionScreen,
            Pair<UsernameScreenCtrl, Parent> usernameScreen,
            Pair<EndScreenCtrl, Parent> endScreen,
            Pair<HelpScreenCtrl, Parent> helpScreen
    ) {
        this.primaryStage = primaryStage;

        this.homeScreenCtrl = homeScreen.getKey();
        this.homeScreenParent = homeScreen.getValue();

        this.waitingRoomCtrl = waitingRoom.getKey();
        this.waitingRoomParent = waitingRoom.getValue();

        this.loadingScreenCtrl = loadingScreen.getKey();
        this.loadingScreenParent = loadingScreen.getValue();

        this.questionScreenCtrl = questionScreen.getKey();
        this.questionScreenParent = questionScreen.getValue();

        this.usernameScreenCtrl = usernameScreen.getKey();
        this.usernameScreenParent = usernameScreen.getValue();

        this.endScreenCtrl = endScreen.getKey();
        this.endScreenParent = endScreen.getValue();

        this.helpScreenCtrl = helpScreen.getKey();
        this.helpScreenParent = helpScreen.getValue();

        // TODO: uncomment to disable the fullscreen popup
        //primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(new Scene(homeScreenParent));
        primaryStage.show();
        primaryStage.setFullScreen(true);
        checkDarkMode();
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

    public void showQuestionScreen() {
        primaryStage.getScene().setRoot(questionScreenParent);
        checkDarkMode();
        questionScreenCtrl.countdown();
    }

    public void showEndScreen() {
        primaryStage.getScene().setRoot(endScreenParent);
        checkDarkMode();
    }

    public void showHelpScreen() {
        primaryStage.getScene().setRoot(helpScreenParent);
        checkDarkMode();
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

    public void setPreviousScreen(int previousScreen) {
        helpScreenCtrl.setPreviousScreen(previousScreen);
    }

}

