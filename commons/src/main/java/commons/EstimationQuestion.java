package commons;

import java.util.List;

import lombok.*;

@Data
/**
 * Class for estimation questions. A player is supposed to estimate which question uses similar
 * amount of energy
 */
public class EstimationQuestion extends Question {
    private List<Activity> activities; // list of activities
    private Long correct_answer; // index of the correct answer in the list

    /**
     * no args constructor
     */
    public EstimationQuestion() {
    }

    /**
     * constructor with the activities
     * @param activities list of 3 activities to chose from
     */
    public EstimationQuestion(List<Activity> activities) {
        this.activities = activities;
        this.correct_answer = generateCorrectAnswer();
    }

    private Long generateCorrectAnswer() {
        if (activities == null || activities.size() <= 0) {
            return -1L;
        }
        Activity current = activities.get(0);
        return current.getConsumption_in_wh();
    }
}
