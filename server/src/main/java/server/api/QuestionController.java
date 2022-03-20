package server.api;

import commons.Activity;
import commons.ComparativeQuestion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ActivityRepository;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final Random random; // Random function from Config file
    private final ActivityRepository repo;

    /**
     * Creates new QuestionController object
     * @param random Random bean from config
     * @param repo repository to use
     */
    public QuestionController(Random random, ActivityRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    /**
     * Generates a random question with 3 random activities
     *
     * @return Comparative question with 3 activities
     */
    @GetMapping(path = {"/comparative", "/comparative/"})
    public ResponseEntity<ComparativeQuestion> getRandomComparative() {

        int limit = 3; // how many activities are included in the question

        if (repo.count() <= limit) { // checks if the repository has enough activities
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        List<Activity> activities = repo.getRandomActivities(limit).get(); // gets 3 random activities
        int n = (int) random.nextLong();
        boolean isMost = n % 2 == 0; // gets a random true or false
        ComparativeQuestion q = new ComparativeQuestion(activities, isMost);
        return ResponseEntity.ok(q);
    }
}
