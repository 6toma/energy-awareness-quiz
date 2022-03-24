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
package client.utils;

import commons.ChangesMessage;
import commons.ComparativeQuestion;
import commons.Player;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Server utilities class
 * Used to connect to the server
 */
public class ServerUtils {

    @Getter
    private static final String defaultURL = "http://localhost:8080/";
    @Getter
    private String serverURL = defaultURL;

    /**
     * Constructor for ServerUtils
     * No need to specifiy any fields
     */
    public ServerUtils() {
    }

    /**
     * Gets a comparative question from the server
     * @return Comparative Question if successful
     */
    public ComparativeQuestion getCompQuestion() {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverURL).path("api/questions/comparative") // the URL path which we HTTP GET for comparative questions
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<>() {});
    }

    /**
     * Posts a Player object to the server
     * @param player to be posted
     * @return The posted player if successful
     */
    public Player postPlayer(Player player){
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverURL).path("api/players/add-one") // the URL path where we HTTP POST for adding high scores
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(player, APPLICATION_JSON), Player.class);
    }

    /**
     * Sets the server URL
     * If input invalid sets url to default
     *
     * @param serverURL
     */
    public void setServerURL(String serverURL) {
        if(serverURL.length() > 0){
            this.serverURL = serverURL;
        } else {
            this.serverURL = defaultURL;
        }
    }

    /**
     * Returns a list of players with the highest scores
     * List returned should be ordered in descending order unless some magic
     *
     * @param numberOfTop determines how many players in list
     * @return A list of top numberOfTop players
     */
    public List<Player> getLeaderPlayers(int numberOfTop){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/players/leaderboard/"+numberOfTop)
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }
    /**
     * Gets all the changes that happened
     * it will be a list of numbers that correspond to certain actions to be taken
     * eg. 1 == we on a different question now, 2 == score of players have changed
     * @return

    public List<ChangesMessage> getGameChanges(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }
    */



    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();
    /**
     * Dont know what this does but apparently
     * its meant to get a change that happened on the server
     * and then based on that change number we send another
     * request but for the body fo the change
     */
    public void registerUpdates(Consumer<ChangesMessage> consumer) {
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig())
                        .target(serverURL).path("")
                        .request(APPLICATION_JSON) //
                        .accept(APPLICATION_JSON) //
                        .get(Response.class);
                if (res.getStatus() == 204){
                    continue;
                }
                var c = res.readEntity(ChangesMessage.class);
                consumer.accept(c);
            }
        });
    }

    /**
     * Closes the Thread
     */
    public void stop(){
        EXEC.shutdownNow();
    }

    /**
     * Used to sync up scenes with Server
     * @return
     */
    public int getCurrentScene(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Used to sync up "which question we are on" with server
     * @return
     */
    public int getCurrentQuestionNumber(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Used to send your score to the Server Multplayer Game Object
     * @param player
     * @return
     */
    public Player postScore(Player player){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(player, APPLICATION_JSON), Player.class);
    }

    /**
     * When the toilet is flushed we get the whole game object where
     * we take the questions and players from
     * @return

    public MultiplayerGame getMultiplayerGame(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }
    */

    /**
     * Gets the Game id
     * Dont know why
     * Its in the diagram that was created
     * @return
     */
    public Long getGameID(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Gets all players so you can display their names and score
     * on the leaderboard
     * @return
     */
    public List<Player> getPlayerChanges(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }
}
