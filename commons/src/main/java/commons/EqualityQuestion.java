package commons;

import lombok.Data;

import java.util.List;
import java.util.Random;

/**
 * Class for creating Equality Question
 */
@Data
public class EqualityQuestion implements Question {

    private List<Activity> activities;
    private int correct_answer;
    private Activity chosen;

    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public EqualityQuestion() {
    }

    /**
     * Constructor which specifies a list of activities
     *
     * @param activities ist of activities
     */
    public EqualityQuestion(List<Activity> activities) {
        this.activities = activities;
        this.chosen = chooseActivity();
        this.correct_answer = generateCorrectAnswer();
    }

    private Activity chooseActivity() {
        Random rand = new Random();
        int upperbound = 2;
        int int_random = rand.nextInt(upperbound);

        Activity chosen = activities.get(int_random);
        return chosen;
    }


    private int generateCorrectAnswer() {
        if (activities == null || activities.size() <= 0) {
            return -1;
        }
        int initial = Math.toIntExact(chosen.getConsumption_in_wh());
        int other = Integer.MIN_VALUE;
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i) != chosen) {
                other = Math.toIntExact(activities.get(i).getConsumption_in_wh());
                break;
            }
        }
        return initial / other;
    }

}
