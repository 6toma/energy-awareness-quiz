package server.api;

import commons.Activity;
import commons.ComparativeQuestion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ActivityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final Random random; // Random function from Config file
    private final ActivityRepository repo;

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

        if (repo.numberDistinctConsumptions() <= limit) { // checks if the repository has enough activities with distinct consumptions
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        List<Activity> activities = activitiesWithSuitableConsumptions(limit); // gets 3 random activities

        int n = (int) random.nextLong();
        boolean isMost = n % 2 == 0; // gets a random true or false
        ComparativeQuestion q = new ComparativeQuestion(activities, isMost);
        return ResponseEntity.ok(q);
    }

    /**
     * Fetches a number of activities, such that they have distinct consumptions
     *
     * @param size The number of activities with distinct consumption to be fetched from the database
     * @return A list of activities
     */
    private List<Activity> activitiesWithSuitableConsumptions(int size) {
        //base case
        if (size == 1) {
            List<Activity> result = new ArrayList<Activity>();
            result.add(repo.getRandomActivity().get());
            return result;
        }

        List<Activity> result = activitiesWithSuitableConsumptions(size - 1);

        //try to get an activity with a comparable consumption
        Optional<Activity> addition = getSuitableActivity(result, 100);
        //if unsuccessful - just get a random one
        if(addition.isEmpty()) addition = getActivityDistinctConsumption(result);

        result.add(addition.get());
        return result;
    }

    private Optional<Activity> getSuitableActivity(List<Activity> list, int tries) {
        Optional<Activity> addition;
        boolean suitable;
        int counter = 0;

        do {
            suitable = true;
            addition = repo.getRandomActivity();
            for (Activity activity : list) {
                if (addition.get().getConsumption_in_wh() == activity.getConsumption_in_wh()
                        || addition.get().getConsumption_in_wh() < list.get(0).getConsumption_in_wh() * 0.5
                        || addition.get().getConsumption_in_wh() > list.get(0).getConsumption_in_wh() * 1.5) suitable = false;
            }
            counter++;
        } while (!suitable && counter != tries);

        //if the max number of tries is reached, but no suitable activity is found - return empty
        if(counter == tries && !suitable) return Optional.empty();
        return addition;
    }

    private Optional<Activity> getActivityDistinctConsumption(List<Activity> list) {
        Optional<Activity> addition;
        boolean distinct;

        do {
            distinct = true;
            addition = repo.getRandomActivity();
            for (Activity activity : list) {
                if (addition.get().getConsumption_in_wh() == activity.getConsumption_in_wh()) distinct = false;
            }
        } while (!distinct);

        return addition;
    }
}
