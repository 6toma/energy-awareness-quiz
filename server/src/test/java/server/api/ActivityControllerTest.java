package server.api;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for activity controller
 */
class ActivityControllerTest {

    private TestActivityRepository repo;
    private Random random;
    private ActivityController act;

    private List<Activity> activities;

    /**
     * Sets up a new activity controller with testing dependencies
     */
    @BeforeEach
    public void setup(){
        random = new TestRandom();
        repo = new TestActivityRepository();
        act = new ActivityController(random, repo);

        activities = List.of(
                new Activity(1L,"a", 1, "a"),
                new Activity(2L,"b", 2, "b"),
                new Activity(3L,"c", 3, "c")
        );
    }

    @Test
    public void getAllTestMany(){
        repo.activities.addAll(activities);
        assertEquals(activities, act.getAll());
    }

    @Test
    public void getAllTestEmpty(){
        List<Activity> expected = new ArrayList<>();
        assertEquals(expected, act.getAll());
    }

    @Test
    public void getActivityByIdTest(){
        repo.activities.addAll(activities);
        assertEquals(activities.get(0), act.getActivityById(1).getBody());
        assertEquals(activities.get(1), act.getActivityById(2).getBody());
        assertEquals(activities.get(2), act.getActivityById(3).getBody());
    }

    @Test
    public void getActivityByIdTestInvalidId(){
        assertEquals(ResponseEntity.badRequest().build(), act.getActivityById(8L));
    }

    @Test
    public void addActivityTest(){
        Activity added = new Activity("a", 1, "a");
        Activity expected = new Activity(1L,"a", 1, "a");
        assertEquals(expected, act.addActivity(added).getBody());
        assertEquals(expected, repo.activities.get(0));
    }

    @Test
    public void addActivityTestNull(){
        assertEquals(ResponseEntity.badRequest().build(), act.addActivity(null));
    }

    @Test
    public void getRandomActivityTest(){
        repo.activities.addAll(activities);
        assertEquals(activities.get(0), act.getRandomActivity().getBody());
        assertEquals(activities.get(1), act.getRandomActivity().getBody());
        assertEquals(activities.get(2), act.getRandomActivity().getBody());
    }

    @Test
    public void deleteActivityTest(){
        repo.activities.addAll(activities);
        assertEquals(activities.get(1), act.deleteActivity(2L).getBody());
        assertEquals(List.of(activities.get(0), activities.get(2)), repo.activities);
    }
}