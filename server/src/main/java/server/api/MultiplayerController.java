package server.api;

import commons.GameUpdatesPacket;
import commons.MultiPlayerGame;
import commons.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.Config;
import server.database.ActivityRepository;
import server.multiplayer.WaitingRoom;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;


@RestController
@RequestMapping("/api")

public class MultiplayerController {

    /**
     * List of everything thats different from last poll
     * For each thing in list we send another request for the body of that change
     */
    private MultiPlayerGame multiplayerGame;
    private final WaitingRoom waitingRoom;
    private final QuestionController questionController;

    /**
     * Creates a Polling Controller
     * @param multiplayerGame injected instance of MultiPlayerGame
     * @param waitingRoom injected instance of WaitingRoom
     * @param random injected instance of Random
     * @param repo injected instance of ActivityRepository
     */
    @Autowired
    public MultiplayerController(MultiPlayerGame multiplayerGame, WaitingRoom waitingRoom, Random random, ActivityRepository repo){
        this.multiplayerGame = multiplayerGame;
        this.waitingRoom = waitingRoom;
        this.questionController = new QuestionController(random, repo);
        generateQuestions();
    }

    /**
     * Returns the instance of the game to the client
     * @return Multiplayer Game object
     */
    @GetMapping("/poll/start-multiplayer")
    public ResponseEntity<Boolean> startGame(){
        if(waitingRoom.getQuestions().size() < Config.numberOfQuestions){
            return ResponseEntity.ok(false);
        }
        multiplayerGame = waitingRoom.flushWaitingRoom();
        multiplayerGame.setCurrentScreen("LOADING SCREEN");
        listeners.forEach((k,l) -> l.accept(multiplayerGame.getGameStatus()));
        sendQuestionToClients(3000);
        generateQuestions();
        return ResponseEntity.ok(true);
    }


    private void sendQuestionToClients(int delay){
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(multiplayerGame.getQuestionNumber() < Config.numberOfQuestions - 1){
                    multiplayerGame.setCurrentScreen("QUESTION");
                    multiplayerGame.nextQuestion();
                    GameUpdatesPacket packet = multiplayerGame.getGameStatus();
                    System.out.println(packet);
                    listeners.forEach((k,l) -> l.accept(packet));
                    sendLeaderboardToClients(19000);
                } else {
                    endMultiplayerGame();
                }
            }
        };

        timer.schedule(task, delay);
    }

    private void sendLeaderboardToClients(int delay){
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                multiplayerGame.setCurrentScreen("LEADERBOARD");
                GameUpdatesPacket packet = multiplayerGame.getGameStatus();
                System.out.println(packet);
                listeners.forEach((k,l) -> l.accept(packet));
                sendQuestionToClients(4000);
            }
        };

        timer.schedule(task, delay);
    }

    private void endMultiplayerGame(){
        multiplayerGame.setCurrentScreen("ENDSCREEN");
        GameUpdatesPacket packet = multiplayerGame.getGameStatus();
        System.out.println(packet);
        listeners.forEach((k,l) -> l.accept(packet));
        multiplayerGame = null;
    }

    /**
     * Returns the instance of the game to the client
     * @return Multiplayer Game object
     */
    @GetMapping("/poll/multiplayer")
    public ResponseEntity<MultiPlayerGame> getGame(){
        return ResponseEntity.ok(multiplayerGame);
    }

    /**
     * Returns a list of player objects
     * to iterate over and get their
     * name and score
     * @return list of players
     */
    @GetMapping("/poll/players")
    public ResponseEntity<List<Player>> getPlayers(){
        List<Player> playerList = multiplayerGame.getPlayers();
        Collections.sort(playerList);
        return ResponseEntity.ok(playerList);
    }

    /**
     * Adds the player to the list of players in the instance of WaitingRoom
     * updates listener to accept number 1. 1 meaning the number of players changed
     * @param player player to be added to the game
     * @return player that was added
     */
    @PostMapping(path={"/poll/add-player-waiting-room"})
    public ResponseEntity<Integer> postPlayerToWaitingRoom(@RequestBody Player player){

        if(player == null) {
            return ResponseEntity.ok(null);
        }
        for(var p : waitingRoom.getPlayers()){
            if(p.getName().equals(player.getName())) {
                return ResponseEntity.ok(null);
            }
        }
        waitingRoom.addPlayerToWaitingRoom(player);
        System.out.println("Player added");
        listeners.forEach((k,l) -> l.accept(new GameUpdatesPacket(waitingRoom.getPlayers().hashCode(), "WAITINGROOM", -1)));
        return ResponseEntity.ok(waitingRoom.getMultiplayerGameID());
        //s.get(players.size()-1)
    }
    /**
     * Endpoint for removing a player from a waiting room
     * @return True if the player was removed successfully
     *         otherwise return false
     */
    @PostMapping(path = {"/poll/remove-player-waiting-room"})
    public ResponseEntity<Boolean> removePlayerFromWaitingRoom(@RequestBody Player player) {
        boolean result = waitingRoom.removePlayerFromWaitingRoom(player);
        listeners.forEach((k,l) -> l.accept(new GameUpdatesPacket(waitingRoom.getPlayers().hashCode(), "WAITINGROOM", -1)));
        System.out.println("Player has been removed");
        System.out.println(waitingRoom.getPlayers());
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint for removing a player from a game
     * @return True if the player was removed successfully
     *         otherwise return false
     */
    @PostMapping(path = {"/poll/remove-player"})
    public ResponseEntity<Boolean> removePlayerFromMultiplayer(@RequestBody Player player) {
        boolean result = multiplayerGame.removePlayer(player);
        System.out.println("Player has been removed from MP");
        System.out.println(multiplayerGame.getPlayers());
        return ResponseEntity.ok(result);
    }

    /**
     * //ToDo: change this depending on needs and multiplayegame class implementation
     * Depending on implementation of MultiPlayerGame class this might be obsolete
     * Server updates the score of the player
     * @param player The player that had its score changed
     * @return the same player with updated score
     */
    @PostMapping(path = {"/poll/send-score"})
    public ResponseEntity<Player> updateScore(@RequestBody Player player){
        Player playerInGame = null;
        List<Player> playerList = multiplayerGame.getPlayers();
        for(int i = 0; i < playerList.size(); i++){
            Player p = playerList.get(i);
            if(p.getName().equals(player.getName())){
                playerInGame = p;
                break;
            }
        }

        if (playerInGame == null){
            return ResponseEntity.badRequest().build();
        }
        playerInGame.setScore(player.getScore());
        System.out.println("New score: " + player);
        return ResponseEntity.ok(player);
    }

    private Map<Object, Consumer<GameUpdatesPacket>> listeners = new HashMap<>();
    /**
     * Gets a number that corresponds to what has changed
     * @return Integer or 204 error
     */
    @GetMapping("/poll/update")
    public DeferredResult<ResponseEntity<GameUpdatesPacket>> getUpdate(){
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<GameUpdatesPacket>>(5000L,noContent);
        var key = new Object();
        listeners.put(key,c ->{
            res.setResult(ResponseEntity.ok(c));
        });
        res.onCompletion(()-> {
            listeners.remove(key);
        });
        return res;
    }

    /**
     * ----------------------------------------- WAITING ROOM METHODS --------------------------------------------------
     */

    /**
     * Endpoint for a list of players from a waiting room
     * @return The list of players currently in the waiting room
     */
    @GetMapping(path = {"/waiting-room/all-players"})
    public ResponseEntity<List<Player>> getWaitingRoomPlayers() {
        return ResponseEntity.ok(waitingRoom.getPlayers());
    }


    /**
     * Endpoint for checking whether a player with a username already exists
     *
     * @return The player added iff the username is unique
     * otherwise return null which means that a player with such username exists
     */
    @PostMapping(path = {"/waiting-room/username"})
    public ResponseEntity<Boolean> isValidUsername(@RequestBody String username) {
        if ("".equals(username) || username == null) {
            return ResponseEntity.ok(false);
        }
        for(var p : waitingRoom.getPlayers()){
            if(p.getName().equals(username)) {
                //System.out.println("bad username");
                return ResponseEntity.ok(false);
            }
        }
        //System.out.println("good username");
        return ResponseEntity.ok(true);
    }


    /**
     * Generates a list of questions in a separate thread
     */
    private void generateQuestions(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(waitingRoom.getQuestions().size() != Config.numberOfQuestions){
                    System.out.println("Question size before: " + waitingRoom.getQuestions().size());
                    System.out.println("NOT GENERATED");
                    int count = Config.numberOfQuestions;
                    while (count > 0) {
                        boolean isAdded = waitingRoom.addQuestion(questionController.getRandomQuestion().getBody());
                        if(isAdded) count--;
                        System.out.println(Config.numberOfQuestions - count);
                    }
                } else {
                    System.out.println("ALREADY GENERATED");
                }
                System.out.println("Question size after: " + waitingRoom.getQuestions().size());
            }
        }).start();
    }
}
