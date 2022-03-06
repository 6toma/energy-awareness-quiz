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

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerByName(@PathVariable("id") Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = {"/add", "/add/"})
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {

        // checks if the object is not null and that it has a name and score specified
        // as this gets sent straight to the database this should always have those properties specified
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

    @PostMapping("/update/{id}")
    public ResponseEntity<Player> updateActivity(@RequestBody Player player, @PathVariable("id") Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        if (player == null || isNullOrEmpty(player.getName())
                || isNullOrEmpty(player.getScore())) {
            return ResponseEntity.badRequest().build();
        }

        player.setId(id);  // set the id of the record to be changed
        repo.save(player); // update the activity
        return ResponseEntity.ok(player);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Player> deleteActivity(@PathVariable("id") Long id) {
        if(!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Player deleted = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(deleted);
    }


}
