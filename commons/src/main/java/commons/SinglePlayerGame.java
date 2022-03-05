package commons;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Single player game class
 * Methods and fields regarding a single player game
 */
public class SinglePlayerGame {

    private ArrayList<Activity> activities;
    private Player player;
    private int streak = 1; // streak field for getting more points when you answer questions in a row
    private int questionNumber = 1;

    public SinglePlayerGame() {};

    // to be implemented
    public SinglePlayerGame(Player player) {
        this.player = player;
        for(int i = 0 ; i < 20; i++){
//            this.activities.addRandomActivity();
        }
    }

    /**
     * This method adds points to a score of a player
     * A particular formula for the points has been developed.
     * parsing an int equal to 2137 will be qualified as answering the question wrongly
     * @param timeWhenAnswered time in seconds how long it took a user to answer a question
     */
    public void addPoints(int timeWhenAnswered){
        if(timeWhenAnswered == 2137){
            return;
        }
        int currentScore = player.getScore();
        player.setScore( currentScore + getPointsToBeAdded(timeWhenAnswered) );
    }

    /**
     * The points achievable for a single question are from 950 to 1050 depending on the number of seconds it took the user to select an answer
     * Each second subtracts 5 points from the added score
     * The score is then multiplied by (100 + streak)% and added to the score of a player
     * @param time
     * @return
     */
    public int getPointsToBeAdded(int time) {
        double streakFactor = (100 + streak) / 100;
        var points = streakFactor * (1050 - 5 * time);
        return (int)points;
    }

    /**
     * increments the question counter
     */
    public void nextQuestion() {
        questionNumber++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SinglePlayerGame)) return false;
        SinglePlayerGame that = (SinglePlayerGame) o;
        return getStreak() == that.getStreak() && getQuestionNumber() == that.getQuestionNumber() && Objects.equals(getActivities(), that.getActivities()) && Objects.equals(getPlayer(), that.getPlayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getActivities(), getPlayer(), getStreak(), getQuestionNumber());
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public Player getPlayer() {
        return player;
    }

    public int getStreak() {
        return streak;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
}
