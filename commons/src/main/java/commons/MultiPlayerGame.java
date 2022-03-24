package commons;

import lombok.Data;

import java.util.List;

/**
 * Multiplayer game class
 * Handles a list of players instead of just one
 */
@Data
public class MultiPlayerGame {

    private int gameID;
    private List<Question> questions;
    private List<Player> players;
    private int questionNumber = 1;

    // for synchronization of client with server
    // can be "LOADING SCREEN", "QUESTION", "LEADERBOARD", "ENDSCREEN"
    private String currentScreen;

    /**
     * Empty constructor
     * Used by Jackson to initialize object from JSON
     */
    public MultiPlayerGame() {}

    /**
     * Creates a new game with specified amount of questions
     * @param players list of players
     * @param questions list of questions for the game
     */
    public MultiPlayerGame(int gameID, List<Player> players, List<Question> questions) {
        this.gameID = gameID;
        this.currentScreen = "LOADING SCREEN";
        this.players = players;
        this.questions = questions;
    }

    /**
     * Adds a question to the list. Checks for duplicates
     * @param question to be added
     * @return true if question was added, false otherwise
     */
    public boolean addQuestion(Question question){
        // TODO: Make this comparison actually do something, right now it uses Object's equals method. Probably should use a set or something
        if(questions.contains(question))
            return false;
        questions.add(question);
        return true;
    }

    /**
     * This method adds points to the scores of every player
     * @param answerTimes a list containing the time measured in seconds each player
     *                    took to answer a question
     * @param guessQuestionRates a list containing the rates on how good the guess was.
     *                           for all other questions besides the estimation one, this will
     *                           be set to 1.0
     */
    public void addPointsForEveryone(List<Integer> answerTimes, List<Double> guessQuestionRates) {
        for(int i = 0; i < players.size(); i++) {
            addPointsForPlayer(answerTimes.get(i), guessQuestionRates.get(i), players.get(i));
        }
    }

    /**
     * This method adds points to a score of a player
     * A particular formula for the points has been developed.
     * parsing an int equal to -1 will be qualified as answering the question wrongly
     * @param timeWhenAnswered time in seconds how long it took a user to answer a question
     * @param guessQuestionRate for every other question than an estimation question this will be set to 1.0
     *                          for the estimation question this will be set to a percentage how good the guess was
     */
    public int addPointsForPlayer(int timeWhenAnswered, double guessQuestionRate, Player player){
        if(timeWhenAnswered == -1){
            player.resetStreak();
            nextQuestion();
            return 0;
        }
        player.incrementStreak();
        int currentScore = player.getScore();
        int pointsToBeAdded = (int)Math.round(guessQuestionRate * getPointsToBeAdded(timeWhenAnswered, player));
        player.setScore( currentScore + pointsToBeAdded );
        return pointsToBeAdded;
    }

    /**
     * The points achievable for a single question are from 950 to 1050 depending on the number of seconds it took the user to select an answer
     * Each second subtracts 5 points from the added score
     * The score is then multiplied by (100 + streak)% and added to the score of a player
     * @param time time it took the player to answer the question
     * @param player player whose score is being calculated
     * @return the points that have to be added to the player's score
     */
    public int getPointsToBeAdded(int time, Player player) {
        double streakFactor = (100.0 + player.getStreak()) / 100.0;
        var points = Math.round(streakFactor * (1050 - 5 * time));
        return (int)points;
    }

    /**
     * Set the current screen
     * @param screen the screen to be set
     */
    public void setCurrentScreen(String screen) {
        switch (screen) {
            case "ENDSCREEN":
            case "LEADERBOARD":
            case "LOADING SCREEN":
            case "QUESTION":
                this.currentScreen = screen;
                break;
            default:
                System.out.println("Not valid screen");
        }
    }

    /**
     * Increments questionNumber
     */
    public void nextQuestion() {
        questionNumber++;
    }

}
