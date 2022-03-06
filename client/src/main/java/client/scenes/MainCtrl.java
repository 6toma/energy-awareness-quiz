package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
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

    // default initializing code
    public void initialize(
            Stage primaryStage,
            Pair<HomeScreenCtrl, Parent> homeScreen,
            Pair<WaitingRoomCtrl, Parent> waitingRoom,
            Pair<LoadingScreenCtrl, Parent> loadingScreen,
            Pair<QuestionScreenCtrl, Parent> questionScreen
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

        // TODO: uncomment to disable the fullscreen popup
        //primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(new Scene(homeScreenParent));
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }

    public void showHomeScreen() {
        primaryStage.getScene().setRoot(homeScreenParent);
    }

    public void showWaitingRoom() {
        primaryStage.getScene().setRoot(waitingRoomParent);
    }

    public void showLoadingScreen() {
        primaryStage.getScene().setRoot(loadingScreenParent);
        loadingScreenCtrl.countdown();
    }

    public void showQuestionScreen() {
        primaryStage.getScene().setRoot(questionScreenParent);
        questionScreenCtrl.countdown();
    }

}

