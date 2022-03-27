package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class WaitingRoomCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private VBox listOfPlayers;
    @FXML
    private Button back;

    /**
     * Creates a new screen with injections
     * @param server ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public WaitingRoomCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Resets the username screen text
     * Goes back to the home screen
     */
    public void back() {
        mainCtrl.resetUserText();
        mainCtrl.showHomeScreen();
    }

    /**
     * start listening for updates
     */
    public void startListening(){
        server.registerUpdates(c -> {
            System.out.println("object identity: "+c+" has changed");
            System.out.println(server.getPlayersMultiplayer().get(0));
        });
    }

    /**
     * stops the thread used for long polling
     */
    public void stop(){
        server.stop();
    }


}
