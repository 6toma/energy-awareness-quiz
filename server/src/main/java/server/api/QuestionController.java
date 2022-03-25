package server.api;

import commons.Activity;
import commons.ComparativeQuestion;
import commons.EstimationQuestion;
import commons.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.Config;
import server.database.ActivityRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final Random random; // Random function from Config file
    private final ActivityRepository repo;

    /**
     * Creates new QuestionController object
     *
     * @param random Random bean from config
     * @param repo   repository to use
     */
    public QuestionController(Random random, ActivityRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = {"/random", "/random/"})
    public ResponseEntity<Question> getRandomQuestion() {

        int randomInt = random.nextInt();
        int numberOfQuestions = 2;

        System.out.println(randomInt);

        // To add more question types increment numberOfQuestions and add another if statement
        // e.g. else if(randomInt % numberOfQuestions == 1) return ...
        if(randomInt % numberOfQuestions == 0){
            return getRandomComparative();
        } else {
            return getRandomEstimation();
        }
    }

    /**
     * Generates a random question with 3 random activities
     * Initializes the image for the activities
     * @return Comparative question with 3 activities
     */
    @GetMapping(path = {"/comparative", "/comparative/"})
    public ResponseEntity<Question> getRandomComparative() {

        int limit = 3; // how many activities are included in the question

        if (repo.numberDistinctConsumptions() <= limit) { // checks if the repository has enough activities with distinct consumptions
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        List<Activity> activities = activitiesWithSuitableConsumptions(limit); // gets 3 random activities

        int n = (int) random.nextLong();
        boolean isMost = n % 2 == 0; // gets a random true or false
        ComparativeQuestion q = new ComparativeQuestion(activities, isMost);
        for(Activity a : q.getActivities()){
            a.initializeImage(new File(Config.defaultImagePath + a.getImage_path()));
        }
        return ResponseEntity.ok(q);
    }

    /**
     * endpoint for getting an activity for a Estimation question
     * @return
     */
    @GetMapping(path = {"/estimation", "/estimation/"})
    public ResponseEntity<Question> getRandomEstimation() {
        int limit = 1;

        if (repo.count() <= limit) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        var activity = repo.getRandomActivities(limit).get().get(0);
        activity.initializeImage(new File(Config.defaultImagePath + activity.getImage_path()));
        EstimationQuestion q = new EstimationQuestion(activity);
        return ResponseEntity.ok(q);
    }

    /**
     * Fetches a number of activities, such that they have distinct consumptions
     *
     * @param limit The number of activities with distinct consumption to be fetched from the database
     * @return A list of activities
     */
    private List<Activity> activitiesWithSuitableConsumptions(int limit) {
        double lowerBound = 0.5;
        double upperBound = 1.5;
        List<Activity> result = new ArrayList<>();
        // list of the IDs of the selection of activities returned by the SQL query
        Optional<List<String>> ids;
        /* pivot may not be included in final selection
        it is used just to have an estimate of the consumption the activities should have */
        Activity pivot = repo.getRandomActivities(1).get().get(0);

        /* get a list of random activities with consumption in the interval
         * (lowerBound * pivot.getConsumption_in_wh(), upperBound * pivot.getConsumption_in_wh())
         *  if the list doesn't have the needed number of activities, increase the range
         */
        do {
            ids = repo.activitiesWithSpecifiedConsumption(
                    limit,
                    (int) Math.floor(lowerBound * pivot.getConsumption_in_wh()),
                    (int) Math.ceil(upperBound * pivot.getConsumption_in_wh())
            );
            lowerBound -= 0.05; //it is not a problem for lowerBound to go below 0
            upperBound += 0.2;
        } while (ids.isEmpty() || ids.get().size() < limit);
        //the while condition ensures exactly the needed number of IDs are returned and not less

        for (int i = 0; i < ids.get().size(); i++) {
            result.add(repo.findById(ids.get().get(i)).get());
        }

        return result;
    }

}
