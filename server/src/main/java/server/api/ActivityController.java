package server.api;

import commons.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import server.database.ActivityRepository;

import java.util.List;
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

    @GetMapping(path = {"", "/"})
    public List<Activity> getAll() {
        return repo.findAll();
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

        Activity act = repo.save(activity); // saves activity to the database
        return ResponseEntity.ok(act); // returns the same object if everything ok
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


    /* This snippet of code is based on the random nextInt(n) implementation
    It does  the same but for Longs and for this part of code specifically
    There is no function to get random next Long with an upperbound
    should later create a sepereate function for this
     */
    private static Long randomLongBounded(Random random, Long upperBound){
        Long randomLong = random.nextLong();;
        Long randomBoundedLong = randomLong % upperBound;
        while (randomLong-randomBoundedLong+(upperBound-1) < 0L){
            randomLong = (random.nextLong() << 1) >>> 1;
            randomBoundedLong = randomLong % upperBound;
        }
        return randomBoundedLong;
    }

    @GetMapping("/random")
    public ResponseEntity<Activity> getRandomActivity(){
        if (repo.count()==0){ // checks if the repository is empty
            return ResponseEntity.badRequest().build();
        }
        Activity act = repo.findById(randomLongBounded(random, repo.count())+1).get();  // +1 because the random long generates a long from 0 to
        return ResponseEntity.ok(act);                                                  // to repo size exclusive
    }

}
