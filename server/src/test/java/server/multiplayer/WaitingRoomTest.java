package server.multiplayer;


import commons.*;
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
        assertEquals(1, waitingRoom.getPlayers().size());
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

    /**
     * Tests if a question is added successfully
     */
    @Test
    public void testAddQuestion() {
        assertTrue(waitingRoom.addQuestion(new ComparativeQuestion(null, false)));

        assertEquals(1, waitingRoom.getQuestions().size());

        Question q = new ComparativeQuestion(null, true);
        assertTrue(waitingRoom.addQuestion(q));
        assertEquals(2, waitingRoom.getQuestions().size());
    }

    /**
     * Tests if many questions are added successfully
     * in a row
     */
    @Test
    public void addManyQuestions() {
        waitingRoom.addQuestion(new ComparativeQuestion(null, true));
        waitingRoom.addQuestion(new EstimationQuestion(a1));
        waitingRoom.addQuestion(new ComparativeQuestion(null, false));
        waitingRoom.addQuestion(new EstimationQuestion(a2));

        assertEquals(4, waitingRoom.getQuestions().size());

        Question q1 = new ComparativeQuestion(List.of(a1, a2), false);
        assertTrue(waitingRoom.addQuestion(q1));

        assertEquals(5, waitingRoom.getQuestions().size());

        assertFalse(waitingRoom.addQuestion(q1));

        assertEquals(5, waitingRoom.getQuestions().size());

        assertFalse(waitingRoom.addQuestion(null));

        assertEquals(5, waitingRoom.getQuestions().size());
    }

    @Test
    void FlushWaitingRoom1(){
        MultiPlayerGame m = waitingRoom.flushWaitingRoom();
        assertNotNull(m);
    }

    @Test
    void FlushWaitingRoom2(){
        MultiPlayerGame m = waitingRoom.flushWaitingRoom();
        assertEquals(0, m.getGameID());
        assertEquals(1, waitingRoom.getWaitingRoomId());
    }
}