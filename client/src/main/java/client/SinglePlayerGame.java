package client;


import commons.Player;
import commons.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Single player game class
 * Used for handling a single-player game
 */
public class SinglePlayerGame {

    private List<Question> questions;
    private Player player;
    private int streak = 0; // streak field for getting more points when you answer questions correctly in a row
    private int questionNumber = 1;
    private int maxQuestions;

    public SinglePlayerGame() {}

    public SinglePlayerGame(int maxQuestions) {
        this.player = new Player();
        this.questions = new ArrayList<>();
        this.maxQuestions = maxQuestions;
    }

    public SinglePlayerGame(int maxQuestions, String username) {
        this.player = new Player(username);
        this.questions = new ArrayList<>();
        this.maxQuestions = maxQuestions;
    }

    public SinglePlayerGame(Player player) {
        this.player = player;
        this.questions = new ArrayList<>();
    }

    public SinglePlayerGame(Player player, List<Question> questions) {
        this.player = player;
        this.questions = questions;
    }

    public boolean addQuestion(Question question){
        // TODO: Make this comparison actually do something, right now it uses Object's equals method. Probably should use a set or something
        for(int i = 0; i < questions.size(); i++) {
            if (question.equals(questions.get(i))) return false;
        }
        questions.add(question);
        return true;
    }

    /**
     * This method adds points to a score of a player
     * A particular formula for the points has been developed.
     * parsing an int equal to -1 will be qualified as answering the question wrongly
     * @param timeWhenAnswered time in seconds how long it took a user to answer a question
     * @param guessQuestionRate for every other question than a guess question this will be set to 1.0
     *                          for the guess question this will be set to a percentage how good the guess was
     */
    public int addPoints(int timeWhenAnswered, double guessQuestionRate){
        if(timeWhenAnswered == -1){
            resetStreak();
            nextQuestion();
            return 0;
        }
        incrementStreak();
        int currentScore = getPlayer().getScore();
        int pointsToBeAdded = (int)Math.round(guessQuestionRate * getPointsToBeAdded(timeWhenAnswered));
        player.setScore( currentScore + pointsToBeAdded );
        nextQuestion();
        return pointsToBeAdded;
    }



    /**
     * The points achievable for a single question are from 950 to 1050 depending on the number of seconds it took the user to select an answer
     * Each second subtracts 5 points from the added score
     * The score is then multiplied by (100 + streak)% and added to the score of a player
     * @param time
     * @return
     */
    public int getPointsToBeAdded(int time) {
        double streakFactor = (100.0 + streak) / 100.0;
        var points = Math.round(streakFactor * (1050 - 5 * time));
        return (int)points;
    }

    /**
     * Methods for manipulating streak and question numbers
     */
    public void nextQuestion(){ questionNumber++;}

    public void resetStreak() { streak = 0;}

    public void incrementStreak() { streak++;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SinglePlayerGame)) return false;
        SinglePlayerGame that = (SinglePlayerGame) o;
        return getStreak() == that.getStreak() && getQuestionNumber() == that.getQuestionNumber() && Objects.equals(questions, that.questions) && Objects.equals(getPlayer(), that.getPlayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(questions, getPlayer(), getStreak(), getQuestionNumber());
    }

    public Player getPlayer() {
        return player;
    }

    public int getStreak() {
        return streak;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getQuestionNumber() { return questionNumber; }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getMaxQuestions() {
        return maxQuestions;
    }

    public void setMaxQuestions(int maxQuestions) {
        this.maxQuestions = maxQuestions;
    }
}
