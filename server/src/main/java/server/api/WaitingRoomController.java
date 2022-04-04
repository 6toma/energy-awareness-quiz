package server.api;

import commons.Activity;
import commons.Player;
import commons.questions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Config;
import server.database.ActivityRepository;
import server.multiplayer.WaitingRoom;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Endpoints and methods for the waiting room
 */

@RestController
@RequestMapping("/api/waiting-room")
public class WaitingRoomController {

    private WaitingRoom waitingRoom;
    private final Random random; // Random function from Config file
    private final ActivityRepository repo;

    /**
     * Creates a WaitingRoomController
     *
     * @param waitingRoom injected instance of Waiting Room
     */
    @Autowired
    public WaitingRoomController(WaitingRoom waitingRoom, Random random, ActivityRepository repo) {
        this.waitingRoom = waitingRoom;
        this.random = random;
        this.repo = repo;
    }

    /**
     * Endpoint for a list of players from a waiting room
     * @return The list of players currently in the waiting room
     */
    @GetMapping(path = {"all-players"})
    public ResponseEntity<List<Player>> getWaitingRoomPlayers() {
        return ResponseEntity.ok(waitingRoom.getPlayers());
    }


    /**
     * Endpoint for checking whether a player with a username already exists
     *
     * @return The player added iff the username is unique
     * otherwise return null which means that a player with such username exists
     */
    @PostMapping(path = {"username"})
    public ResponseEntity<Boolean> isValidUsername(@RequestBody String username) {
        if ("".equals(username) || username == null) {
            return ResponseEntity.ok(false);
        }
        for(var p : waitingRoom.getPlayers()){
            if(p.getName().equals(username)) {
                //System.out.println("bad username");
                return ResponseEntity.ok(false);
            }
        }
        //System.out.println("good username");
        return ResponseEntity.ok(true);
    }


    // METHODS FROM GENERATING QUESTIONS

    /**
     * endpoint for checking whether a list of questions has been genarated
     *
     * @return true if the questions have already been generated
     * false if have not yet been generated
     */
    @GetMapping(path = {"are-generated"})
    public ResponseEntity<Boolean> areQuestionsGenerated() {
        if(waitingRoom.getQuestions().size() != Config.numberOfQuestions){
            System.out.println("Question size before: " + waitingRoom.getQuestions().size());
            System.out.println("NOT GENERATED");
            int count = Config.numberOfQuestions;
            while (count > 0) {
                boolean isAdded = waitingRoom.addQuestion((Question) getRandom().getBody());
                if(isAdded) count--;
                System.out.println(Config.numberOfQuestions - count);
            }

            return ResponseEntity.ok(false);
        }
        System.out.println("Question size after: " + waitingRoom.getQuestions().size());

        System.out.println("ALREADY GENERATED");
        return ResponseEntity.ok(true);
    }


    // METHODS FROM QuestionController

    /**
     * Generates a random number, uses it to get a random question type
     *
     * @return either:
     * - ResponseEntity precondition failed
     * - ComparativeQuestion
     * - EstimationQuestion
     * - MC question
     * - Equality question
     */
    public ResponseEntity<Question> getRandom() {
        int randomInt = random.nextInt();
        int numberOfQuestions = 4;

        // To add more question types increment numberOfQuestions and add another if statement
        // e.g. else if(randomInt % numberOfQuestions == 1) return ...
        /*if (randomInt % numberOfQuestions == 0) {
            return getRandomComparative();
        } else if(randomInt % numberOfQuestions == 1) {*/
            return getRandomEstimation();
        /*} else if(randomInt % numberOfQuestions == 2){
            return getRandomMCQuestion();
        } else {
            return getRandomEquality();
        }*/
    }

    /**
     * Generates a random question with 3 random activities
     * Initializes the image for the activities
     *
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
        for (Activity a : q.getActivities()) {
            a.initializeImage(new File(Config.defaultImagePath + a.getImage_path()));
        }
        return ResponseEntity.ok(q);
    }

    /**
     * Generates a random question with 3 energy values
     * Gets 3 random activities with similar consumptions
     * Selects one of those, uses the other activities' consumptions
     * We use other activities for consumptions to make the numbers feel more natural than randomly generated ones
     * Initializes the image for the activity
     *
     * @return MC Question with 3 values
     */
    @GetMapping(path = {"/mc", "/mc/"})
    public ResponseEntity<Question> getRandomMCQuestion() {
        int limit = 3;

        if (repo.numberDistinctConsumptions() <= limit) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
        List<Activity> activities = activitiesWithSuitableConsumptions(limit); // gets 3 random activities
        // Chooses the first activity as the correct answer
        Activity activity = activities.get(0);
        // Make a list of the other activities' consumptions
        List<Long> options = List.of(activities.get(1).getConsumption_in_wh(), activities.get(2).getConsumption_in_wh());

        activity.initializeImage(new File(Config.defaultImagePath + activity.getImage_path()));

        Question q = new MCQuestion(activity, options);
        return ResponseEntity.ok(q);
    }

    /**
     * endpoint for getting an activity for an Estimation question
     *
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
     * Generates a random equality question
     * Initializes the image for the activities
     *
     * @return Equality Question considering 2 activities
     */
    @GetMapping(path = {"/equality", "/equality/"})
    public ResponseEntity<Question> getRandomEquality() {

        int limit = 4; // we need at least 4 activities to have this question without all having distinct consumptions
        if (repo.count() <= limit || repo.numberDistinctConsumptions() == repo.count()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        // Gets a random activity which doesn't have a unique consumption
        Activity chosen = repo.nonUniqueActivities(1).get().get(0);
        // Gets a random activity different from chosen which has the same consumption as chosen
        Activity correct = repo.sameConsumptionActivities(chosen.getConsumption_in_wh(), chosen.getId(), 1).get().get(0);

        List<Activity> activities = activitiesWithSuitableConsumptionsGenerator(2, correct); // gets 2 random activities

        // Creates a new question with a chosen, correct and list of wrong activities. Specifies the position of correct in the list of options
        // Randomizing needs to be done here for testing
        EqualityQuestion q = new EqualityQuestion(chosen, correct, activities, Math.abs(random.nextInt() % 3));
        // Initialize images for the answer options
        for (Activity a : q.getActivities()) {
            a.initializeImage(new File(Config.defaultImagePath + a.getImage_path()));
        }
        return ResponseEntity.ok(q);

    }

    /**
     * Fetches a number of activities, such that they have distinct consumptions
     * Generates a pivot to be used in the generation of such activities
     * <p>
     * Split into 2 methods to input specific pivot to the generator
     *
     * @param limit The number of activities with distinct consumption to be fetched from the database
     * @return A list of activities
     */
    private List<Activity> activitiesWithSuitableConsumptions(int limit) {

        /* pivot may not be included in final selection
        it is used just to have an estimate of the consumption the activities should have */
        Activity pivot = repo.getRandomActivities(1).get().get(0);
        return activitiesWithSuitableConsumptionsGenerator(limit, pivot);
    }

    /**
     * Fetches a number of activities, such that they have distinct consumptions
     *
     * @param limit The number of activities with distinct consumption to be fetched from the database
     * @param pivot Pivot that the result list has close consumptions to
     * @return A list of activities
     */
    private List<Activity> activitiesWithSuitableConsumptionsGenerator(int limit, Activity pivot) {
        double lowerBound = 0.5;
        double upperBound = 1.5;
        List<Activity> result = new ArrayList<>();
        // list of the IDs of the selection of activities returned by the SQL query
        Optional<List<String>> ids;

        /* get a list of random activities with consumption in the interval
         * (lowerBound * pivot.getConsumption_in_wh(), upperBound * pivot.getConsumption_in_wh())
         *  if the list doesn't have the needed number of activities, increase the range
         */
        do {
            ids = repo.activitiesWithSpecifiedConsumption(
                    limit,
                    (int) Math.floor(lowerBound * pivot.getConsumption_in_wh()),
                    (int) Math.ceil(upperBound * pivot.getConsumption_in_wh()),
                    pivot.getConsumption_in_wh()
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

