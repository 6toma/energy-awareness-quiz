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
import server.multiplayer.WaitingRoom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;


@RestController
@RequestMapping("/api/poll")

public class LongPollController {

    /**
     * List of everything thats different from last poll
     * For each thing in list we send another request for the body of that change
     */
    private MultiPlayerGame multiplayerGame;
    private final WaitingRoom waitingRoom;
    /**
     * Creates a Polling Controller
     * @param multiplayerGame injected instance of MultiPlayerGame
     * @param waitingRoom injected instance of WaitingRoom
     */
    @Autowired
    public LongPollController(MultiPlayerGame multiplayerGame, WaitingRoom waitingRoom, GameUpdatesPacket gameUpdatesPacket){
        this.multiplayerGame = multiplayerGame;
        this.waitingRoom = waitingRoom;
    }

    /**
     * Returns the instance of the game to the client
     * @return Multiplayer Game object
     */
    @GetMapping("start-multiplayer")
    public ResponseEntity<Boolean> startGame(){
        if(waitingRoom.getQuestions().size() < Config.numberOfQuestions){
            return ResponseEntity.ok(false);
        }
        multiplayerGame = waitingRoom.flushWaitingRoom();
        multiplayerGame.setCurrentScreen("LOADING SCREEN");
        listeners.forEach((k,l) -> l.accept(multiplayerGame.getGameStatus()));
        startMultiplayerLogic();
        return ResponseEntity.ok(true);
    }

    /**
     * this doesnt work
     */
    private void startMultiplayerLogic(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(multiplayerGame.getQuestionNumber() < Config.numberOfQuestions){
                    multiplayerGame.setCurrentScreen("QUESTION");
                    multiplayerGame.nextQuestion();
                } else {
                    multiplayerGame.setCurrentScreen("ENDSCREEN");
                    cancel();
                }
                GameUpdatesPacket state = multiplayerGame.getGameStatus();
                System.out.println(state);
                listeners.forEach((k,l) -> l.accept(state));
            }
        };

        timer.schedule(task, 3000, 6*1000);
    }

    /**
     * Returns the instance of the game to the client
     * @return Multiplayer Game object
     */
    @GetMapping("multiplayer")
    public ResponseEntity<MultiPlayerGame> getGame(){
        return ResponseEntity.ok(multiplayerGame);
    }

    /**
     * gets the name of the current screen of the server
     * "LOADING SCREEN", "QUESTION", "LEADERBOARD", "ENDSCREEN"
     * @return the name of current screen
     */
    @GetMapping("CurrentScreen")
    public ResponseEntity<String> getCurrentScreen(){
        return ResponseEntity.ok(multiplayerGame.getCurrentScreen());
    }

    /**
     * Gets the question currently on 'ID'
     * @return integer of which question server is on eg 3 (out of 20)
     */
    @GetMapping("CurrentQuestionNumber")
    public ResponseEntity<Integer> getCurrentQuestion(){
        return ResponseEntity.ok(multiplayerGame.getQuestionNumber());
    }

    /**
     * Returns a list of player objects
     * to iterate over and get their
     * name and score
     * @return list of players
     */
    @GetMapping("players")
    public ResponseEntity<List<Player>> getPlayers(){
        return ResponseEntity.ok(multiplayerGame.getPlayers());
    }

    /**
     * Adds the player to the list of players in the instance of WaitingRoom
     * updates listener to accept number 1. 1 meaning the number of players changed
     * @param player player to be added to the game
     * @return player that was added
     */
    @PostMapping(path={"add-player-waiting-room"})
    public ResponseEntity<Integer> postPlayerToWaitingRoom(@RequestBody Player player){

        listeners.forEach((k,l) -> l.accept(new GameUpdatesPacket(waitingRoom.getPlayers().hashCode(), "WAITINGROOM", -1)));
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
        return ResponseEntity.ok(waitingRoom.getMultiplayerGameID());
        //s.get(players.size()-1)
    }
    /**
     * Endpoint for removing a player from a waiting room
     * @return True if the player was removed successfully
     *         otherwise return false
     */
    @PostMapping(path = {"remove-player-waiting-room"})
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
    @PostMapping(path = {"remove-player"})
    public ResponseEntity<Boolean> removePlayerFromMultiplayer(@RequestBody Player player) {
        boolean result = multiplayerGame.removePlayer(player);
        listeners.forEach((k,l) -> l.accept(multiplayerGame.getGameStatus()));
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
    @PostMapping(path = {"SendScore"})
    public ResponseEntity<Player> updateScore(@RequestBody Player player){
        int indexPlayer = multiplayerGame.getPlayers().indexOf(player);
        if (indexPlayer==-1){
            return ResponseEntity.badRequest().build();
        }
        multiplayerGame.getPlayers().get(indexPlayer).setScore(player.getScore());
        return ResponseEntity.ok(player);
    }

    private Map<Object, Consumer<GameUpdatesPacket>> listeners = new HashMap<>();
    /**
     * Gets a number that corresponds to what has changed
     * @return Integer or 204 error
     */
    @GetMapping("/update")
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
     * Currently obsolete but give undesrtanting of how it will work
     * Returns a number that's 2^x which describes what is
     * different compared to what the client sent
     * so the client can request for those specific objects
     * instead of the whole game object
     * @param change number whose bits represent change
     * @return an Integer that describes the change
     */
    /*@PostMapping(path = {"give-update/{change}"})
    public ResponseEntity<Integer> giveUpdate(@PathVariable("change") Integer change){
        listeners.forEach((k,l) -> l.accept(change));
        return ResponseEntity.ok(change);
    }*/

    /**
     * Cool piece of code found, it redirects so it keeps polling
     * obsolete right now but could be used in the future
     * @param input the input of the previous endpoint
     * @return the same thing as input
     * @throws InterruptedException interrupts something
     */
    /*
    private ResponseEntity<List<Integer>> keepPolling(Integer input) throws InterruptedException {
        Thread.sleep(5000);
        HttpHeaders headers = new HttpHeaders();
        //"/getMessages?id=" + input.getID() + "&type=" + input.getType()
        headers.setLocation(URI.create("/getMessages?id=" + input + "&type=" + input));
        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }
     */

}
