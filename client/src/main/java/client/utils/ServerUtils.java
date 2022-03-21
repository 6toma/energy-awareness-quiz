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

import commons.ComparativeQuestion;
import commons.Player;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import lombok.Getter;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

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
}
