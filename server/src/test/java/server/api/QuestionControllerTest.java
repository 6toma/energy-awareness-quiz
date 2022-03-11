package server.api;

import commons.Activity;
import commons.ComparativeQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionControllerTest {

    private TestRandom random;
    private TestActivityRepository repo;

    private List<Activity> activities;
    private QuestionController que;

    /**
     * Sets up a new question controller with testing dependencies
     */
    @BeforeEach
    public void setup(){
        random = new TestRandom();
        repo = new TestActivityRepository();
        que = new QuestionController(random, repo);

        activities = List.of(
            new Activity("1", "image_a","a", 1, "a"),
            new Activity("2", "image_b","b", 2, "b"),
            new Activity("3", "image_c","c", 3, "c"),
            new Activity("4", "image_d","d", 4, "d"),
            new Activity("5", "image_e","e", 5, "e"),
            new Activity("6", "image_f","f", 6, "f"),
            new Activity("7", "image_g","g", 7, "g")
        );

        repo.activities.addAll(activities);
    }

    @Test //repo and que have different TestRandom objects!
    void getRandomComparativeTest() {
        List<Activity> e1_list = List.of(activities.get(0), activities.get(1), activities.get(2));
        ComparativeQuestion expected1 = new ComparativeQuestion(e1_list, true);
        assertEquals(expected1, que.getRandomComparative().getBody());

        List<Activity> e2_list = List.of(activities.get(3), activities.get(4), activities.get(5));
        ComparativeQuestion expected2 = new ComparativeQuestion(e2_list, false);
        assertEquals(expected2, que.getRandomComparative().getBody());
    }
}