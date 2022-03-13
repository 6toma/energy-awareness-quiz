/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.google.inject.Guice.createInjector;

/**
 * Main class for client application
 */
public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule()); // dependency injection stuff
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch(); //runs start()
    }

    /**
     * Starts the stage (window)
     *
     * @param primaryStage
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        // loading the overview and add quote scenes
        /*
        var overview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml");
        var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, overview, add); //main controller gets 2 scenes it can switch between
         */

        var homeScreen = FXML.load(
                HomeScreenCtrl.class,
                "scenes/HomeScreen.fxml",
                "css/HomeScreen.css"
        );
        var waitingRoom = FXML.load(
                WaitingRoomCtrl.class,
                "scenes/WaitingRoom.fxml",
                "css/WaitingRoom.css"
        );
        var loadingScreen = FXML.load(
                LoadingScreenCtrl.class,
                "scenes/LoadingScreen.fxml",
                "css/LoadingScreen.css"
        );
        var comparativeQuestionScreen = FXML.load(
                ComparativeQuestionScreenCtrl.class,
                "scenes/ComparativeQuestionScreen.fxml",
                "css/ComparativeQuestionScreen.css"
        );
        var usernameScreen = FXML.load(
                UsernameScreenCtrl.class,
                "scenes/UsernameScreen.fxml",
                "css/main.css"
        );
        var endScreen = FXML.load(
                EndScreenCtrl.class,
                "scenes/EndScreen.fxml",
                "css/EndScreen.css"
        );
        var helpScreen = FXML.load(
                HelpScreenCtrl.class,
                "scenes/HelpScreen.fxml",
                "css/HelpScreen.css"
        );

        // add more scenes the same way

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, homeScreen, waitingRoom, loadingScreen, comparativeQuestionScreen, usernameScreen, endScreen, helpScreen);
    }
}
