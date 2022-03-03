package commons;

import java.util.List;
import java.util.Objects;

/**
 * Class for storing Comparative questions in the format:
 *      Which activity uses the most/least energy?
 *          Activity 1
 *          Activity 2
 *          Activity 3
 */
public class ComparativeQuestion {

    private boolean isMost; // true if the question asks for most, false if least energy
    private List<Activity> activities; // list of activities
    private int correct_answer; // index of the correct answer in the list

    public ComparativeQuestion(List<Activity> activities, boolean isMost){
        this.isMost = isMost;
        this.activities = activities;
        this.correct_answer = generateCorrectAnswer();
    }

    public int generateCorrectAnswer() {
        if(activities == null || activities.size() <= 0){
            return -1;
        }
        int min = 0; // index of minimum
        int max = 0; // index of maximum
        for(int i = 1; i < activities.size(); i++){

            Activity current = activities.get(i);
            if(current.getConsumption_in_wh() < activities.get(min).getConsumption_in_wh() ){
                min = i; // check if current has smaller consumption than min
            }
            if(current.getConsumption_in_wh() > activities.get(max).getConsumption_in_wh() ){
                max = i; // checks if current has bigger consumption than max
            }
        }
        if(isMost){
            return max;
        } else {
            return min;
        }
    }

    @Override
    public String toString() {
        return "ComparativeQuestion{" +
            "isMost=" + isMost +
            ", activities=" + activities +
            ", correct_answer=" + correct_answer +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComparativeQuestion that = (ComparativeQuestion) o;
        return isMost == that.isMost && correct_answer == that.correct_answer && Objects.equals(activities, that.activities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isMost, activities, correct_answer);
    }

    public boolean isMost() {
        return isMost;
    }

    public void setMost(boolean most) {
        isMost = most;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public int getCorrect_answer() { // no setter for this because it's generated
        return correct_answer;
    }
}
