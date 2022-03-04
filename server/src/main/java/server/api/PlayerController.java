package server.api;

import commons.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.PlayerRepository;
import java.util.List;


/**
 * Activity endpoints go in this controller
 */

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerRepository repo;

    @Autowired
    public PlayerController(PlayerRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = {"", "/"})
    public List<Player> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{name}")
    public ResponseEntity<Player> getPlayerByName(@PathVariable("name") String name) {
        if (!repo.existsById(name)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(name).get());
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        // checks if the json is of a proper player
        if (player == null || isNullOrEmpty(player.getName())
                || isNullOrEmpty(player.getScore())) {
            return ResponseEntity.badRequest().build();
        }

        Player savedPlayer = repo.save(player);
        return ResponseEntity.ok(savedPlayer);
    }

    private static boolean isNullOrEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            String s = (String) o;
            return s.isEmpty();
        }
        if (o instanceof Integer) {
            Integer s = (Integer) o;
            return s<0;
        }
        return false;
    }
    /**
     Api endpoint for updating an Activity in the database by specifying an id in the path
     */
    @PostMapping("/update/{name}")
    public ResponseEntity<Player> updateActivity(@RequestBody Player player, @PathVariable("name") String name) {
        if (!repo.existsById(name)) {
            return ResponseEntity.badRequest().build();
        }
        if (player == null || isNullOrEmpty(player.getName())
                || isNullOrEmpty(player.getScore())) {
            return ResponseEntity.badRequest().build();
        }

        player.setName(name);  // set the id of the record to be changed
        repo.save(player); // update the activity
        return ResponseEntity.ok(player);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Player> deleteActivity(@PathVariable("name") String name) {
        if(!repo.existsById(name)) {
            return ResponseEntity.badRequest().build();
        }
        Player deleted = repo.findById(name).get();
        repo.deleteById(name);
        return ResponseEntity.ok(deleted);
    }


}
