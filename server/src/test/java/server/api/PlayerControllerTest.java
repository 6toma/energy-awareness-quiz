package server.api;

import commons.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.api.dependencies.TestPlayerRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerControllerTest {

    private TestPlayerRepository repo;
    private PlayerController ply;

    private List<Player> players;

    /**
     * Sets up a new activity controller with testing dependencies
     * Runs before every test
     */
    @BeforeEach
    public void setup(){
        repo = new TestPlayerRepository();
        ply = new PlayerController(repo);

        players = List.of(
            new Player(1L,"a", 1),
            new Player(2L,"b", 2),
            new Player(3L,"c", 3)
        );
    }

    /**
     * Test for getAll with many players
     */
    @Test
    void getAllTest() {
        repo.players.addAll(players);
        assertEquals(players, ply.getAll());
    }

    /**
     * Test for getAll with no players
     */
    @Test
    void getAllTestEmpty() {
        List<Player> expected = new ArrayList<>();
        assertEquals(expected, ply.getAll());
    }

    /**
     * Test for getPlayerById
     */
    @Test
    void getPlayerByIdTest() {
        repo.players.addAll(players);
        assertEquals(players.get(0), ply.getPlayerById(1L).getBody());
        assertEquals(players.get(1), ply.getPlayerById(2L).getBody());
        assertEquals(players.get(2), ply.getPlayerById(3L).getBody());
    }

    /**
     * Test for getPLayerById with invalid id
     */
    @Test
    void getPlayerByIdTestInvalid() {
        assertEquals(ResponseEntity.badRequest().build(), ply.getPlayerById(8L));
    }

    /**
     * Test for adding a player
     */
    @Test
    void addPlayerTest() {
        Player added = new Player("a", 1);
        Player expected = new Player(1L, "a", 1);
        assertEquals(expected, ply.addPlayer(added).getBody());
        assertEquals(expected, repo.players.get(0));
    }

    /**
     * Test for adding a null player
     */
    @Test
    void addPlayerTestNull() {
        assertEquals(ResponseEntity.badRequest().build(), ply.addPlayer(null));
    }

    /**
     * Test for updateing a player
     */
    @Test
    void updatePlayerTest() {
        repo.players.addAll(players);
        Player update = new Player();
        update.setName("d");
        update.setScore(4);

        Player expected = new Player(2L, "d", 4);
        assertEquals(expected, ply.updateActivity(update, 2L).getBody());
        assertEquals(expected, repo.players.get(1));
    }

    /**
     * Test for updating a player with invalid player
     */
    @Test
    void updatePlayerTestNull() {
        repo.players.addAll(players);
        assertEquals(ResponseEntity.badRequest().build(), ply.updateActivity(null, 2L));
        assertEquals(players, repo.players);
    }

    /**
     * Test for updating a player with invalid id
     */
    @Test
    void updatePlayerTestInvalidId() {
        repo.players.addAll(players);
        Player update = new Player();
        assertEquals(ResponseEntity.badRequest().build(), ply.updateActivity(update, 8L));
        assertEquals(players, repo.players);
    }

    /**
     * Test for updating a player name
     */
    @Test
    void updatePlayerTestName() {
        repo.players.addAll(players);
        Player update = new Player();
        update.setName("d");

        Player expected = new Player(2L, "d", 2);
        assertEquals(expected, ply.updateActivity(update, 2L).getBody());
        assertEquals(expected, repo.players.get(1));
    }

    /**
     * Test for updating a player score
     */
    @Test
    void updatePlayerTestScore() {
        repo.players.addAll(players);
        Player update = new Player();
        update.setScore(4);

        Player expected = new Player(2L, "b", 4);
        assertEquals(expected, ply.updateActivity(update, 2L).getBody());
        assertEquals(expected, repo.players.get(1));
    }

    /**
     * Test for updating a player with empty player
     */
    @Test
    void updatePlayerTestEmpty() {
        repo.players.addAll(players);
        Player update = new Player();

        assertEquals(ResponseEntity.badRequest().build(), ply.updateActivity(update, 2L));
        assertEquals(players, repo.players);
    }

    /**
     * Test for deleting a player
     */
    @Test
    void deletePlayerTest() {
        repo.players.addAll(players);
        assertEquals(players.get(1), ply.deleteActivity(2L).getBody());
        assertEquals(List.of(players.get(0), players.get(2)), repo.players);
    }
}