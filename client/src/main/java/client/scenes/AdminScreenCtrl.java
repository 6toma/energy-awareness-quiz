package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

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

    @FXML
    private TableColumn<Activity, String> image_path;

    @FXML
    private TextField inputActivityID;

    @FXML
    private TextField inputActivityTitle;

    @FXML
    private TextField inputActivityConsumption;

    @FXML
    private TextField inputActivitySource;

    @FXML
    private TextField inputActivityImagePath;


    /**
     * Get values into the table
     */
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getId()));
        title.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTitle()));
        source.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSource()));
        consumption.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getConsumption_in_wh().toString()));
        image_path.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getImage_path()));

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

    public void addActivity() {
        Activity activity = checkTextFields();
        if (activity != null) {
            server.addActivity(activity);
            refresh();
            Alert successfulAdd = new Alert(Alert.AlertType.INFORMATION);
            successfulAdd.setHeaderText("The activity has been added successfully!");
            successfulAdd.showAndWait();
        }
    }

    public void deleteActivity() {
        Activity toBeDeleted = activityTable.getSelectionModel().getSelectedItem();
        if (toBeDeleted == null) {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setHeaderText("Please select an activity from the table first");
            noSelection.showAndWait();
            return;
        }
        Alert confirmDeletion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDeletion.setHeaderText("The activity is about to be deleted (no undo)!");
        ((Button) confirmDeletion.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) confirmDeletion.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

        Optional<ButtonType> res = confirmDeletion.showAndWait();
        if (res.get() == ButtonType.OK) {
            server.deleteActivity(toBeDeleted);
        }
        refresh();
    }

    public void editActivity() {
        Activity toBeEdited = activityTable.getSelectionModel().getSelectedItem();
        if (toBeEdited == null) {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setHeaderText("Please select an activity to edit from the table first");
            noSelection.showAndWait();
        }
        inputActivityID.setText(toBeEdited.getId());
        inputActivityImagePath.setText(toBeEdited.getImage_path());
        inputActivityTitle.setText(toBeEdited.getTitle());
        inputActivityConsumption.setText(Long.toString(toBeEdited.getConsumption_in_wh()));
        inputActivitySource.setText(toBeEdited.getSource());
        server.deleteActivity(toBeEdited);
    }

    public void commitEdit() {
        Activity toBeAdded = checkTextFields();
        if (toBeAdded != null) {
            server.addActivity(toBeAdded);
            refresh();
        }
    }


    @SuppressWarnings("checkstyle:EqualsAvoidNull")
    public Activity checkTextFields() {
        String id = inputActivityID.getText();
        String path = inputActivityImagePath.getText();
        String title = inputActivityTitle.getText();
        String source = inputActivitySource.getText();
        if (id.equals("") || path.equals("") || title.equals("")
                || inputActivityConsumption.getText().equals("") || source.equals("")) {
            Alert emptyFields = new Alert(Alert.AlertType.ERROR);
            emptyFields.setHeaderText("Please fill all the text fields with corresponding data.");
            emptyFields.showAndWait();
            return null;
        }
        try {
            long consumption = Long.parseLong(inputActivityConsumption.getText());
            clearFields();
            Activity a = new Activity(id, path, title, consumption, source);
            return a;
        } catch (NumberFormatException e) {
            Alert wrongType = new Alert(Alert.AlertType.ERROR);
            wrongType.setHeaderText("Consumption must be an integer number!");
            wrongType.showAndWait();
            return null;
        }
    }

    public void clearFields() {
        inputActivityID.clear();
        inputActivityImagePath.clear();
        inputActivityTitle.clear();
        inputActivityConsumption.clear();
        inputActivitySource.clear();
    }


}
