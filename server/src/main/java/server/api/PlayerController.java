package server.api;

import commons.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.PlayerRepository;
import java.util.List;
import java.util.Optional;


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
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Returns a list of players with the highest scores
     * List returned should be ordered in descending order unless some magic
     *
     * @param numberOfPlayers determines how many players in list
     * @return A list of top numberOfTop players
     */
    @GetMapping("/leaderboard/{number}")
    public ResponseEntity<List<Player>> getTopPlayers(@PathVariable("number") Long numberOfPlayers) {
        if (repo.count()<numberOfPlayers) { //count() returns a Long
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
        return ResponseEntity.ok(repo.getTopPlayers(numberOfPlayers.intValue()).get());
    }

    @PostMapping(path = {"/add-one", "/add-one/"})
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

        Optional<Player> dbPlayerOpt = repo.findById(id); // get the player from the database

        if (dbPlayerOpt.isEmpty() || player == null // bad request if editable id doesn't exist or no new values given
                || isNullOrEmpty(player.getName()) && isNullOrEmpty(player.getScore())) {
            return ResponseEntity.badRequest().build();
        }
        Player dbPlayer = dbPlayerOpt.get();

        if (!isNullOrEmpty(player.getName())) { // check if a new name is specified
            dbPlayer.setName(player.getName());
        }
        if (!isNullOrEmpty(player.getScore())) { // check if a new score is specified
            dbPlayer.setScore(player.getScore());
        }

        Player saved = repo.save(dbPlayer); // update the player
        return ResponseEntity.ok(saved); // for some reason I can't return dbPlayer, it throws an internal server error
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
