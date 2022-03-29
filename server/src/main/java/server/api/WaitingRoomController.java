package server.api;

import commons.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.multiplayer.WaitingRoom;

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
     * Endpoint for checking whether a player with a username already exists
     * @return True iff hte username exists, then the player has to supply a new one
     *         otherwise false
     */
    @PostMapping(path = {"username"})
    public ResponseEntity<Player> isValidUsername(@RequestBody Player player) {
        for(var p : waitingRoom.getPlayers()){
            if(p.getName().equals(player.getName())) {
                System.out.println("bad username");
                return ResponseEntity.ok(null);
            }
        }
        waitingRoom.addPlayerToWaitingRoom(player);
        System.out.println("good username");
        return ResponseEntity.ok(player);
    }
}

