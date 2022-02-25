package server.api;

import commons.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/")
    public ResponseEntity<Activity> addActivity(@RequestBody Activity activity){

        // checks if the json is of a proper activity
        if(activity == null || isNullOrEmpty(activity.getTitle())
                || isNullOrEmpty(activity.getConsumption_in_wh())
                || isNullOrEmpty(activity.getSource())){

            return ResponseEntity.badRequest().build();
        }
        repo.save(activity);
        // activity.save() or something like that should be here when we connect it to the database
        return ResponseEntity.ok(activity); // returns the same object if everything ok
    }

    private static boolean isNullOrEmpty(Object o) { // checks if an object is null or empty
        if(o == null){
            return true;
        }
        if(o instanceof String){
            String s = (String) o;
            return s.isEmpty();
        }
        return false;
    }
    /**
    Api endpoint for updating an Activity in the database by specifying an id in the path
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<Activity> updateActivity(@RequestBody Activity activity, @PathVariable("id") long id) {
        // check if a id exists in database
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        // checks if the json is of a proper activity
        if(activity == null || isNullOrEmpty(activity.getTitle())
                || isNullOrEmpty(activity.getConsumption_in_wh())
                || isNullOrEmpty(activity.getSource())){

            return ResponseEntity.badRequest().build();
        }

        activity.setId(id);  // set the id of the record to be changed
        repo.save(activity); // update the activity
        return ResponseEntity.ok(activity);
    }

}
