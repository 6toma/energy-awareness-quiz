package server.api;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.api.dependencies.TestActivityRepository;

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
        repo = new TestActivityRepository();
        act = new ActivityController(repo);

        activities = List.of(
                new Activity("1", "image_a","a", 1L, "a"),
                new Activity("2", "image_b","b", 2L, "b"),
                new Activity("3", "image_c","c", 3L, "c")
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
        assertEquals(activities.get(0), act.getActivityById("1").getBody());
        assertEquals(activities.get(1), act.getActivityById("2").getBody());
        assertEquals(activities.get(2), act.getActivityById("3").getBody());
    }

    @Test
    public void getActivityByIdTestInvalidId(){
        assertEquals(ResponseEntity.badRequest().build(), act.getActivityById("8"));
    }

    /*
    @Test
    public void addActivityTest(){
        Activity added = new Activity("image_a","a", 1, "a");
        Activity expected = new Activity("1", "image_a","a", 1, "a");
        assertEquals(expected, act.addActivity(added).getBody());
        assertEquals(expected, repo.activities.get(0));
    }

     */

    @Test
    public void addManyActivitiesTest(){

        List<Activity> added = new ArrayList<>(activities);

        assertEquals(activities.size(), act.addManyActivities(added).getBody());
        assertEquals(activities, repo.activities);
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
        assertEquals(activities.get(1), act.deleteActivity("2").getBody());
        assertEquals(List.of(activities.get(0), activities.get(2)), repo.activities);
    }
}