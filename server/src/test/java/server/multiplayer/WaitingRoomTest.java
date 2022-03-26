package server.multiplayer;


import commons.Activity;
import commons.Player;
import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WaitingRoomTest {

    WaitingRoom waitingRoom;
    List<Question> questions;
    List<Player> players;
    Activity a1;
    Activity a2;

    /**
     * Code to be run before each test, create a list of questions and players
     */
    @BeforeEach
    public void setup() {
        questions = new ArrayList<>();
        players = new ArrayList<>();
        waitingRoom = new WaitingRoom(0, players, questions, 5);
        a1 = new Activity("1L", "image_a", "a", 1L, "a");
        a2 = new Activity("2L", "image_b", "b", 2L, "b");
    }

    /**
     * No arguments constructor test
     */
    @Test
    public void constructorTest1() {
        WaitingRoom w = new WaitingRoom();
        assertNotNull(w);
    }

    /**
     * Constructor with arguments test
     */
    @Test
    void constructorTest2() {
        WaitingRoom w = new WaitingRoom(0, players, questions, 5);
        assertNotNull(w);
    }

    /**
     * Adding players which are not in the list and their names are not null
     */
    @Test
    public void successfulAddPlayerTest(){
        Player p1 = new Player("a");
        Player p2 = new Player("b");
        assertTrue(waitingRoom.addPlayerToWaitingRoom(p1));
        assertTrue(waitingRoom.addPlayerToWaitingRoom(p2));
        assertEquals(2, waitingRoom.getPlayers().size());
    }

    /**
     * Adding a player without name
     */
    @Test
    public void addPlayerWithoutNameTest(){
        Player p1 = new Player();
        assertFalse(waitingRoom.addPlayerToWaitingRoom(p1));
    }

    /**
     * Adding a player who is already in the waiting room
     */
    @Test
    public void AddAnExistingPlayerTest(){
        Player p1 = new Player("a");
        waitingRoom.addPlayerToWaitingRoom(p1);
        assertFalse(waitingRoom.addPlayerToWaitingRoom(p1));
        assertEquals(0, waitingRoom.getPlayers().size());
    }

    /**
     * Remove a player from waiting room
     */
    @Test
    public void RemoveExistingPlayerTest(){
        Player p1 = new Player("a");
        waitingRoom.addPlayerToWaitingRoom(p1);
        assertEquals(1, waitingRoom.getPlayers().size());
        //assertTrue(waitingRoom.removePlayerFromWaitingRoom(p1));
    }

    /**
     * remove player which is not in waiting room
     */
    @Test
    public void RemoveNonExistingPlayerTest(){
        Player p1 = new Player("a");
        assertFalse(waitingRoom.removePlayerFromWaitingRoom(p1));
    }

}