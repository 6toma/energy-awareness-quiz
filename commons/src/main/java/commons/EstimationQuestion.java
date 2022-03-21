package commons;

import java.util.List;
import java.util.Objects;

public class EstimationQuestion extends Question {

    private List<Activity> activities; // list of activities
    private Long correct_answer; // index of the correct answer in the list

    /**
     * Constructor
     */
    public EstimationQuestion() {
    }

    /**
     * Constructor
     *
     * @param activities
     */
    public EstimationQuestion(List<Activity> activities) {
        this.activities = activities;
        this.correct_answer = generateCorrectAnswer();
    }

    /**
     * Generates correct answer for the question
     *
     * @return
     */
    private Long generateCorrectAnswer() {
        if (activities == null || activities.size() <= 0) {
            return -1L;
        }
        Activity current = activities.get(0);
        return current.getConsumption_in_wh();
    }

    @Override
    public String toString() {
        return "EstimationQuestion{" +
                "activity " + activities +
                ", correct_answer=" + correct_answer +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstimationQuestion that = (EstimationQuestion) o;
        return correct_answer == that.correct_answer && Objects.equals(activities, that.activities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activities, correct_answer);
    }

    /**
     * Getter for activities
     *
     * @return
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * Setter for activities
     *
     * @param activities
     */
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    /**
     * Getter for correct answer
     *
     * @return
     */
    public Long getCorrect_answer() { // no setter for this because it's generated
        return correct_answer;
    }
}
