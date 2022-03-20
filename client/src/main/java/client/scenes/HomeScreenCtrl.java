package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import lombok.Getter;

import java.util.List;


/**
 * Controller used as an intermediate between controllers for all
 * scenes/windows
 * <p>
 * CURRENTLY, USED FOR HOME SCREEN/SCENE ONLY
 */
public class HomeScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    public boolean isLightMode;

    //0 - still on home screen
    //1 - reached username screen by pressing SINGLEPLAYER
    //2 - reached username screen by pressing MULTIPLAYER
    @Getter
    public int usernameOriginScreen;

    // @FXML
    // private Text titleText;
    // could be used to style the title

    @FXML
    private GridPane leaderboard;

    @FXML
    private Button darkMode;

    @FXML
    private TextField inputServerURLField;

    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isLightMode = true;
        this.usernameOriginScreen = 0; //still on home screen
    }

    @FXML
    void toggleDarkMode() {
        isLightMode = !isLightMode;
        if (!isLightMode)
            darkMode.setText("Light Mode");
        else {
            darkMode.setText("Dark Mode");
        }
        mainCtrl.checkDarkMode();
    }

    public boolean getDarkMode() {
        return isLightMode;
    }

    public void setUsernameOriginScreen(int usernameOriginScreen) {
        if (usernameOriginScreen == 0 ||
                usernameOriginScreen == 1 ||
                usernameOriginScreen == 2)
            this.usernameOriginScreen = usernameOriginScreen;
    }

    /**
     * Used to update leaderboard entries
     * Method adds 10 players with highest score to the leaderbaord grid
     * If theres not enough players in the repository, it appends
     * empty players with score 0 to the leaderboard
     */
    @FXML
    public void setPlayer() {
        List<Player> players = server.getLeaderPlayers(10);
        if (players.size()<10){
            for (int i=0; i<(10-players.size());i++){
                players.add(new Player("",0));
            }
        }

        for (int index=0; index< players.size(); index++){
            Label name = new Label();
            name.setText(players.get(index).getName());
            name.setStyle("-fx-font-size: 24px;");
            Label score = new Label();
            score.setText(players.get(index).getScore().toString());
            score.setStyle("-fx-font-size: 24px;");
            this.leaderboard.add(name,1, index+1);
            this.leaderboard.add(score,2, index+1);
        }
    }

    /**
     * Tries to get a question from the server
     * If succeeds connect create a new singlePlayerGame and go to the username screen
     * <p>
     * TODO:
     * - Show error to user if connection to server failed
     */
    @FXML
    public void showUsernameScreenSingle() {

        mainCtrl.getServer().setServerURL(inputServerURLField.getText());
        try {
            mainCtrl.newSinglePlayerGame();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Connection failed");
        }
    }

    @FXML
    public void showUsernameScreenMulti() {

        // Testing ServerUtils postPlayer functionality
        System.out.println(inputServerURLField.getText());
        server.setServerURL(inputServerURLField.getText());
        Player player = new Player("test", 7357);
        try {
            System.out.println(server.postPlayer(player));
        } catch (Exception e) {
            System.err.println("Connection failed");
        }

        mainCtrl.setUsernameOriginScreen(2);
        mainCtrl.showUsernameScreen();
    }

    @FXML
    public void showHelpScreen() {
        mainCtrl.showHelpScreen();
    }

    @FXML
    void exitApp(ActionEvent event) {
        // to fully terminate the client process
        Platform.exit();
        System.exit(0);
    }
}
