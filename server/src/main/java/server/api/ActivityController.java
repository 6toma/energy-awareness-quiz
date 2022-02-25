package server.api;

import commons.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ActivityRepository;

import java.util.Random;

/**
 * Activity endpoints go in this controller
 */

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final Random random; // Random function from Config file
    private final ActivityRepository repo;

    @Autowired
    public ActivityController(Random random, ActivityRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping("/")
    public ResponseEntity<Activity> addActivity(@RequestBody Activity activity) {

        // checks if the json is of a proper activity
        if (activity == null || isNullOrEmpty(activity.getTitle())
            || isNullOrEmpty(activity.getConsumption_in_wh())
            || isNullOrEmpty(activity.getSource())) {

            return ResponseEntity.badRequest().build();
        }

        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved); // returns the same object if everything ok
    }

    private static boolean isNullOrEmpty(Object o) { // checks if an object is null or empty
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            String s = (String) o;
            return s.isEmpty();
        }
        return false;
    }

}
