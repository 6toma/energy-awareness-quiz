package server.api;

import commons.GameUpdatesPacket;
import commons.MultiPlayerGame;
import commons.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiplayerControllerTest {
    private MultiPlayerGame mpg;
    private MultiplayerController lpc;
    private ArrayList<Player> players;

    /**
     * Sets up environment before each test
     */
    @BeforeEach
    public void setup(){
        players = new ArrayList<>();
        players.addAll(List.of(
                new Player(1L,"a", 1),
                new Player(2L,"b", 2),
                new Player(3L,"c", 3)
        ));
        mpg = new MultiPlayerGame(0, players, null);
        lpc = new MultiplayerController(mpg, null, null, null);
    }

    /**
     * Test for get Multiplayer game
     */
    @Test
    void getGameTest() {
        assertEquals(mpg, lpc.getGame(0).getBody());
    }

    /**
     * Test for getting list of all players
     */
    @Test
    void getPlayersTest() {
        assertEquals(players, lpc.getPlayers(0).getBody());
    }

    /**
     * Test for updating score of player
     * Needs Mockito for proper testing
     */
    @Test
    void updateScoreTest() {
        Player newScore = new Player(1L,"a", 5);
        assertEquals(newScore, lpc.updateScore(0, newScore).getBody());
        int indexPlayer = mpg.getPlayers().indexOf(newScore);
        assertEquals(5, mpg.getPlayers().get(indexPlayer).getScore());
        assertEquals(ResponseEntity.badRequest().build(), lpc.updateScore(0, new Player(5L,"bob", 99)));
    }
    /**
     * Test for getting the update packet
     * Needs Mockito for proper testing
     */
    @Test
    void getUpdateTest() throws InterruptedException {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<GameUpdatesPacket>>(5000L,noContent);
        var res2 = lpc.getUpdate(0);
        Thread.sleep(5500);
        assertEquals(res.getClass(), res2.getClass());
    }
}
