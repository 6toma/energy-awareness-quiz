package server.api;

import commons.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.multiplayer.WaitingRoom;
import server.Config;

/**
 * Endpoints and methods for the waiting room
 */

@RestController
@RequestMapping("/api/waiting-room")
public class WaitingRoomController {

    private WaitingRoom waitingRoom;

    /**
     * Creates a WaitingRoomController
     * @param waitingRoom injected instance of MultiPlayerGame
     */
    @Autowired
    public WaitingRoomController(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    /**
     * Endpoint for adding a player to a waiting room
     * @return The player is added iff the username is unique
     *         otherwise return null which means that a player with such username exists
     */
    @PostMapping(path = {"player"})
    public ResponseEntity<Player> isValidPlayer(@RequestBody Player player) {
        if(player == null) {
            return ResponseEntity.ok(null);
        }
        for(var p : waitingRoom.getPlayers()){
            if(p.getName().equals(player.getName())) {
                System.out.println("bad player");
                return ResponseEntity.ok(null);
            }
        }
        waitingRoom.addPlayerToWaitingRoom(player);
        System.out.println("good player");
        return ResponseEntity.ok(player);
    }

    /**
     * Endpoint for checking whether a player with a username already exists
     * @return The player added iff the username is unique
     *         otherwise return null which means that a player with such username exists
     */
    @PostMapping(path = {"username"})
    public ResponseEntity<Boolean> isValidUsername(@RequestBody String username) {
        if("".equals(username) || username == null) {
            return ResponseEntity.ok(false);
        }
        for(var p : waitingRoom.getPlayers()){
            if(p.getName().equals(username)) {
                System.out.println("bad username");
                return ResponseEntity.ok(false);
            }
        }
        System.out.println("good username");
        return ResponseEntity.ok(true);
    }

    /**
     * endpoint for checking whether a list of questions has been genarated
     * @return true if the questions have already been generated
     *         false if have not yet been generated
     */
    @GetMapping(path = {"are-generated"})
    public ResponseEntity<Boolean> areQuestionsGenerated() {
        if(waitingRoom.getQuestions().size() != Config.numberOfQuestions){
            System.out.println("NOT GENERATED");
            int count = Config.numberOfQuestions;
            //  while(count > 0){
            //      var Questions =
            //  }
            return ResponseEntity.ok(false);
        }
        System.out.println("GENERATED");
        return ResponseEntity.ok(true);
    }

}

