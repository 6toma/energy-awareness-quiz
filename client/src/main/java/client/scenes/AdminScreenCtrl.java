package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class AdminScreenCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<Activity> data;

    /**
     * Creates a new screen with injections
     *
     * @param server   ServerUtils class
     * @param mainCtrl Main Controller
     */
    @Inject
    public AdminScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Returns to the home screen
     */
    public void goToHomeScreen() {
        mainCtrl.showHomeScreen();
    }

    @FXML
    private TableView<Activity> activityTable;

    @FXML
    private TableColumn<Activity, String> id;

    @FXML
    private TableColumn<Activity, String> title;

    @FXML
    private TableColumn<Activity, String> source;

    @FXML
    private TableColumn<Activity, String> consumption;


    /**
     * Get values into the table
     */
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getId()));
        title.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTitle()));
        source.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSource()));
        consumption.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getConsumption_in_wh().toString()));

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, 1000);
    }

    /**
     * Refresh admin screen
     */
    @FXML
    public void refresh() {
        try {
            List<Activity> activityList = server.getAllActivities();
            ObservableList<Activity> observableArrayList =
                    FXCollections.observableArrayList(activityList);
            activityTable.setItems(observableArrayList);
            for (Activity a : activityTable.getItems()) {
                System.out.println(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
