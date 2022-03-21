package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    @FXML
    private StackPane parentContainer;

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

    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public HomeScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isLightMode = true;
        this.usernameOriginScreen = 0; //still on home screen
    }

    /**
     * Gets the value of isLightMode
     * @return false if is in dark mode (??)
     */
    public boolean getDarkMode() {
        return isLightMode;
    }

    /**
     * Sets the origin of the username selection screen
     * @param usernameOriginScreen 1 if going to single player
     *                             2 if going to multiplayer
     */
    public void setUsernameOriginScreen(int usernameOriginScreen) {
        if (usernameOriginScreen == 0 ||
                usernameOriginScreen == 1 ||
                usernameOriginScreen == 2)
            this.usernameOriginScreen = usernameOriginScreen;
    }


    /**
     * This method transfers the user to the settings screen
     * where he/she can switch to dark mode, read the help page,
     * enter the room URL or go to admin panel
     * @param event
     */
    @FXML
    void goToSettings(ActionEvent event) {
        mainCtrl.showSettingsScreen();
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
        /*if (players.size()<10){
            for (int i=0; i<(10-players.size());i++){
                players.add(new Player("Empty",0));
            }
        }*/
        int childrenSize = this.leaderboard.getChildren().size();
        if(childrenSize > 13){
            this.leaderboard.getChildren().remove(13,childrenSize);
        }
        for (int index=0; index< players.size(); index++){
            Label name = new Label();
            name.setText(players.get(index).getName());
            Label score = new Label();
            score.setText(players.get(index).getScore().toString());
            setGridNodeStyle(name, score,index);

            this.leaderboard.add(name,1, index+1);
            this.leaderboard.add(score,2, index+1);
        }
    }

    private void setGridNodeStyle(Label name, Label score, int index){
        if (index==0){
            name.setStyle("-fx-background-color: gold;");
            score.setStyle("-fx-background-color: gold;");
        } else if (index==1){
            name.setStyle("-fx-background-color: silver;");
            score.setStyle("-fx-background-color: silver;");
        } else if (index==2){
            name.setStyle("-fx-background-color: CD7F32;");
            score.setStyle("-fx-background-color: CD7F32;");
        }
        name.getStyleClass().add("grid-Label");
        score.getStyleClass().add("grid-Label");
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

        mainCtrl.getServer().setServerURL(mainCtrl.getServerURL());
        try {
            mainCtrl.newSinglePlayerGame();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Connection failed");
        }
    }

    /**
     * Run when pressed multiplayer
     * Sends you to the username selection screen
     */
    @FXML
    public void showUsernameScreenMulti() {

        // Testing ServerUtils postPlayer functionality
        System.out.println(mainCtrl.getServerURL());
        server.setServerURL(mainCtrl.getServerURL());
        Player player = new Player("test", 7357);
        try {
            System.out.println(server.postPlayer(player));
        } catch (Exception e) {
            System.err.println("Connection failed");
        }

        mainCtrl.setUsernameOriginScreen(2);
        mainCtrl.showUsernameScreen();
    }

    /**
     * Shows the help screen
     */
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
