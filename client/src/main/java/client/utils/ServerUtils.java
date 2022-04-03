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

import commons.GameUpdatesPacket;
import commons.MultiPlayerGame;
import commons.Player;
import commons.questions.Question;
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
     * Gets a random question from the server
     * @return either:
     *         - ComparativeQuestion
     *         - EstimationQuestion
     */
    public Question getRandomQuestion() {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverURL).path("api/questions/random") // the URL path which we HTTP GET for comparative questions
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
     * @param serverURL url to the server to connect to
     */
    public void setServerURL(String serverURL) {
        if (serverURL.length() > 0) {
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


    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();
    /**
     * Dont know what this does but apparently
     * its meant to get a change that happened on the server
     * and then based on that change number we send another
     * request but for the body fo the change
     */
    public void registerUpdates(Consumer<GameUpdatesPacket> consumer) {
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig())
                        .target(serverURL).path("api/poll/update")
                        .request(APPLICATION_JSON) //
                        .accept(APPLICATION_JSON) //
                        .get(Response.class);
                if (res.getStatus() == 204){
                    System.out.println();
                    continue;
                }
                var c = res.readEntity(GameUpdatesPacket.class);
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
     * @return String name of the screen
     */
    public String getCurrentScene(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/poll/CurrentScreen")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Used to sync up "which question we are on" with server
     * @return Integer of the current question
     */
    public int getCurrentQuestionNumber(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/poll/CurrentQuestionNumber")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Used to send your score to the Server Multplayer Game Object
     * @param player player object containing name and score
     * @return the Player that has been posted
     * dont know why this returns anything
     */
    public Player postScore(Player player){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/poll/SendScore") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(player, APPLICATION_JSON), Player.class);
    }

    /**
     * When the toilet is flushed we get the whole game object where
     * we take the questions and players from
     * this should only be done once at the start
     * @return instance of multiplayer game
     */
    public MultiPlayerGame getMultiplayerGame(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/poll/MultiplayerGame")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Gets all players, so you can display their names and score
     * on the leaderboard
     * @return list of players
     */
    public List<Player> getPlayersWaitingRoom(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/waiting-room/all-players")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Checks whether a username is valid i.e. has not been previously used
     * @param username of a player to be added
     * @return True iff can be added else return False
     */
    public Boolean checkValidityOfUsername(String username){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/waiting-room/username")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(username, APPLICATION_JSON), Boolean.class);
    }

    /**
     * Adds player to the WaitingRoom
     * This is needed because the user can change the name in the input field
     * and click continue and without that there could be multiple players with the same name
     * @param player player to be added
     * @return player that was added
     */
    public Player addPlayerWaitingRoom(Player player){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/waiting-room/player")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(player, APPLICATION_JSON), Player.class);
    }

    /**
     * checks whether a list of questions has been generated,
     * generates a new list if the list is empty
     * @return player that was added
     */
    public Boolean areQuestionsGenerated(){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/waiting-room/are-generated")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
    }


    /**
     * Removes a player from waiting room
     * @param player player to be removed
     * @return player that was removed
     */
    public Boolean removePlayerWaitingRoom(Player player){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverURL).path("api/waiting-room/remove-player")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(player, APPLICATION_JSON), Boolean.class);
    }


}
