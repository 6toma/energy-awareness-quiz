package server.api;

import commons.Activity;
import commons.Player;
import commons.questions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.Config;
import server.api.dependencies.TestActivityRepository;
import server.api.dependencies.TestRandom;
import server.multiplayer.WaitingRoom;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaitingRoomControllerTest {

    private TestRandom random;
    private TestActivityRepository repo;
    private WaitingRoom waitingRoom;
    private List<Activity> activities;
    private WaitingRoomController waitingRoomController;
    private List<Question> questions;

    /**
     * Sets up a new Question controller with testing dependencies
     * Runs before every test
     */
    @BeforeEach
    public void setup(){
        random = new TestRandom();
        repo = new TestActivityRepository();
        waitingRoom = new WaitingRoom();
        waitingRoomController = new WaitingRoomController(waitingRoom, random, repo);

        activities = List.of(
                new Activity("1", "image_a","a", 1L, "a"),
                new Activity("2", "image_b","b", 2L, "b"),
                new Activity("3", "image_c","c", 3L, "c"),
                new Activity("4", "image_d","d", 4L, "d"),
                new Activity("5", "image_e","e", 5L, "e"),
                new Activity("6", "image_f","f", 6L, "f"),
                new Activity("7", "image_g","g", 7L, "g")
        );

        questions = new ArrayList<>();
    }

    @AfterEach
    void cleanUp(){
        repo.activities = new ArrayList<>();
        random.setCount(0);
    }

    /**
     * Test for getting a random comparative Question
     * Uses TestRandom implementation so random is predictable
     */
    @Test //repo and waitingRoomController have different TestRandom objects!
    void getRandomComparativeTest() {
        random.setCount(0);
        repo.activities.addAll(activities);

        List<Activity> e1_list = List.of(activities.get(1), activities.get(2), activities.get(3));
        ComparativeQuestion expected1 = new ComparativeQuestion(e1_list, true);
        assertEquals(expected1, waitingRoomController.getRandomComparative().getBody());
    }

    @Test
    void getRandomComparativeTestNoActivities() {
        assertEquals(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build(), waitingRoomController.getRandomComparative());
    }

    @Test
    void getRandomEstimationTest() {
        repo.activities.addAll(activities);

        EstimationQuestion expected = new EstimationQuestion(activities.get(0));
        assertEquals(expected, waitingRoomController.getRandomEstimation().getBody());
    }

    @Test //repo and waitingRoomController have different TestRandom objects!
    void getRandomMCTest() {
        repo.activities.addAll(activities);

        List<Long> e1_list = List.of(activities.get(2).getConsumption_in_wh(), activities.get(3).getConsumption_in_wh());
        MCQuestion expected1 = new MCQuestion(activities.get(1), e1_list);
        assertEquals(expected1, waitingRoomController.getRandomMCQuestion().getBody());
    }

    /**
     * Returns an error because all activities have uniwaitingRoomController consumptions
     */
    @Test
    void getRandomEqualityTestUniQue() {
        repo.activities.addAll(activities);
        assertEquals(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build(), waitingRoomController.getRandomEquality());
    }

    @Test
    void getRandomEqualityTestEmpty() {
        assertEquals(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build(), waitingRoomController.getRandomEquality());
    }

    @Test
    void getRandomEqualityTest() {
        random.setCount(0);

        Activity similar = new Activity("8", "image_h","h", 1L, "h");
        repo.activities.addAll(activities);
        repo.activities.add(similar);

        List<Activity> expectedList = List.of(activities.get(1), activities.get(2));
        EqualityQuestion expected = new EqualityQuestion(activities.get(0), similar, expectedList, 0);

        assertEquals(expected, waitingRoomController.getRandomEquality().getBody());
    }

    @Test
    void getRandomMCTestNoActivities() {
        assertEquals(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build(), waitingRoomController.getRandomMCQuestion());
    }

    @Test
    void getRandomEstimationTestNoActivities() {
        repo.activities.add(activities.get(0));
        assertEquals(ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build(), waitingRoomController.getRandomEstimation());
    }

    @Test
    void getRandomQuestionTestComparative() {
        random.setCount(0); // sets the random to start from 0
        repo.activities.addAll(activities);

        ComparativeQuestion expected = new ComparativeQuestion(List.of(activities.get(1), activities.get(2), activities.get(3)), false);

        assertEquals(expected, waitingRoomController.getRandom().getBody());
    }

    @Test
    void getRandomQuestionTestEstimation() {
        repo.activities.addAll(activities);
        random.setCount(1); // sets the random to start from 1

        EstimationQuestion expected = new EstimationQuestion(activities.get(0));

        assertEquals(expected, waitingRoomController.getRandom().getBody());
    }

    @Test
    void getRandomQuestionTestMC() {
        random.setCount(2); // sets the random to start from 2
        repo.activities.addAll(activities);

        List<Long> e1_list = List.of(activities.get(2).getConsumption_in_wh(), activities.get(3).getConsumption_in_wh());
        MCQuestion expected = new MCQuestion(activities.get(1), e1_list);

        assertEquals(expected, waitingRoomController.getRandom().getBody());
    }

    @Test
    void getRandomQuestionTestEquality() {
        random.setCount(3); // sets the random to start from 3

        Activity similar = new Activity("8", "image_h","h", 1L, "h");
        repo.activities.addAll(activities);
        repo.activities.add(similar);

        List<Activity> expectedList = List.of(activities.get(1), activities.get(2));
        EqualityQuestion expected = new EqualityQuestion(activities.get(0), similar, expectedList, 1);

        assertEquals(expected, waitingRoomController.getRandom().getBody());
    }

    @Test
    void getWaitingRoomPlayersTest() {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        waitingRoom.setPlayers(players);

        assertEquals(players, waitingRoomController.getWaitingRoomPlayers().getBody());
    }

    @Test
    void isValidUsernameTest() {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        waitingRoom.setPlayers(players);

        assertFalse(waitingRoomController.isValidUsername("").getBody());
        assertFalse(waitingRoomController.isValidUsername(null).getBody());
        assertFalse(waitingRoomController.isValidUsername(p1.getName()).getBody());
        assertTrue(waitingRoomController.isValidUsername("p3").getBody());
    }

    @Test
    void questionsGeneratedTest(){
        for(int i = 0; i < Config.numberOfQuestions; i++){
            questions.add(new ComparativeQuestion());
        }
        waitingRoom.setQuestions(questions);
        assertTrue(waitingRoomController.areQuestionsGenerated().getBody());
    }
    /*
    @Test
    void questionsNotGeneratedTest(){
        waitingRoom.setQuestions(questions);

        assertFalse(waitingRoomController.areQuestionsGenerated().getBody());
    }*/
}