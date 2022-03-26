package server.api;

import commons.MultiPlayerGame;
import commons.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


@RestController
@RequestMapping("/api/poll")

public class LongPollController {

    /**
     * List of everything thats different from last poll
     * For each thing in list we send another request for the body of that change
     */
    private final MultiPlayerGame multiplayerGame;

    /**
     * Creates a Polling Controller
     * @param multiplayerGame injected instance of MultiPlayerGame
     */
    @Autowired
    public LongPollController(MultiPlayerGame multiplayerGame){
        this.multiplayerGame = multiplayerGame;
    }

    /**
     * Returns the instance of the game to the client
     * @return Multiplayer Game object
     */
    @GetMapping("MultiplayerGame")
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
     * @return integer of whcih question server is on eg 3 (out of 20)
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
     * //ToDo: change this depending on needs and multiplayegame class implementation
     * Depending on implementation of MultiPlayerGame class this might be obsolete
     * Server updates the score of the player
     * @param player The player that had its score changed
     * @return the same player with updated score
     */
    @PostMapping(path = {"SendScore"})
    public ResponseEntity<Player> postScore(@RequestBody Player player){
        int indexPlayer = multiplayerGame.getPlayers().indexOf(player);
        multiplayerGame.getPlayers().get(indexPlayer).setScore(player.getScore());
        return ResponseEntity.ok(player);
    }

    private Map<Object, Consumer<Integer>> listeners = new HashMap<>();
    /**
     * Gets a number that corresponds to what has changed
     * @return Integer or 204 error
     */
    @GetMapping("/update")
    public DeferredResult<ResponseEntity<Integer>> getUpdate(){
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Integer>>(5000L,noContent);
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
     * Returns a number that's 2^x which describes what is
     * different compared to what the client sent
     * so the client can request for those specific objects
     * instead of the whole game object
     * @param change number whose bits represent change
     * @return an Integer that describes the change
     */
    @PostMapping(path = {"give-update/{change}"})
    public ResponseEntity<Integer> giveUpdate(@PathVariable("change") Integer change){
        listeners.forEach((k,l) -> l.accept(change));
        return ResponseEntity.ok(change);
    }

    /**
     * Cool piece of code found, it redirects so it keeps polling
     * obsolete right now but could be used in the future
     * @param input the input of the previous endpoint
     * @return the same thing as input
     * @throws InterruptedException interrupts something
     */
    private ResponseEntity<List<Integer>> keepPolling(Integer input) throws InterruptedException {
        Thread.sleep(5000);
        HttpHeaders headers = new HttpHeaders();
        //"/getMessages?id=" + input.getID() + "&type=" + input.getType()
        headers.setLocation(URI.create("/getMessages?id=" + input + "&type=" + input));
        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }

}
