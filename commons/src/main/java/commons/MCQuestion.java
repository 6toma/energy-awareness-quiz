package commons;

import lombok.Data;

import java.util.List;
import java.util.Random;

@Data
/**
 * Class for MC Question. A player is supposed to choose how much energy does an activity waste
 */
public class MCQuestion {
    private List<Activity> activities;
    private int correct_answer;
    private Activity chosen;

    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public MCQuestion() {
    }

    /**
     * constructor with an activity
     *
     * @param activities to add to question
     */
    public MCQuestion(List<Activity> activities) {
        this.activities = activities;
        this.chosen = chooseActivity();
        this.correct_answer = Math.toIntExact(chosen.getConsumption_in_wh());
    }


    private Activity chooseActivity() {
        Random rand = new Random();
        int upperbound = 3;
        int int_random = rand.nextInt(upperbound);
        Activity chosen = activities.get(int_random);
        return chosen;
    }
}
