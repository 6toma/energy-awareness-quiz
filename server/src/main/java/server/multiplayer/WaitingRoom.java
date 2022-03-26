package server.multiplayer;

import commons.MultiPlayerGame;
import commons.Player;
import commons.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A Waiting room class that will store the players who are waiting to enter a game. The mechanics of this
 * class can be described by a toilet analogy. All the players once the multiplayer is pressed and dumped into a
 * so-called toilet and once a player presses start a MultiplayerGame class is generated using the attributes
 * of the Waiting room and a multiplayer game is started.
 */
@Data
public class WaitingRoom {
    private int waitingRoomId;          // a waiting room id which is incremented every time a new game is started (flush count)
    private List<Player> players;       // list of players
    private List<Question> questions;   // list of questions
    private int maxNumberOfQuestions;   // maximal number of questions
    /**
     * no args constructor
     */
    public WaitingRoom(){}

    /**
     * constructor with a set id (it should be set to zero)
     * @param maxNumberOfQuestions maximal number of questions
     * @param waitingRoomId id of the waiting room
     */
    public WaitingRoom(int waitingRoomId, List<Player> players,  List<Question> questions, int maxNumberOfQuestions){
        this.players = players;
        this.questions = questions;
        this.waitingRoomId = waitingRoomId;
        this.maxNumberOfQuestions = maxNumberOfQuestions;
    }


    /**
     * Adds a question to the list. Checks for duplicates
     * @param question to be added
     * @return true if question was added, false otherwise
     */
    public boolean addQuestion(Question question){
        // TODO: Make this comparison actually do something, right now it uses Object's equals method. Probably should use a set or something
        for(int i = 0; i < questions.size(); i++) {
            if (question.equals(questions.get(i))) return false;
        }
        questions.add(question);
        return true;
    }

    /**
     * generating a new set of questions
     */
    private List<Question> generateNewQuestions() {
        //TODO Once the question types are implemented we can generate the questions properly
        List<Question> result = new ArrayList<>();
        int count = maxNumberOfQuestions;
        while (count > 0){
         //if(addQuestion(server.getCompQuestion())){ count-- }
        }
        return result;
    }

    /**
     * Adding a player to a waiting room
     * @return true if a players name is unique else returns false
     * @param player player to be added
     */
    public boolean addPlayerToWaitingRoom(Player player){
        if(player.getName() == null) return false;
        for(var p : players){
            if (p.equals(player)) {
                return false;
            }
        }
        players.add(player);
        return true;
    }

    /**
     * This method is used in case a player leaves the waiting room
     * and the player list has to reflect this change
     * @param player player to be removed
     * @return true if the player was removed successfully
     *          false if the player could not be removed or
     *          the player was not in the player list
     */
    public boolean removePlayerFromWaitingRoom(Player player) {
        return this.players.remove(player);
    }

    /**
     * Flushing a WaitingRoom. This will transfer all the players to a MultiplayerGame class
     *
     */
    //TODO Make the class return a MultiplayerGame class once it is created
    public MultiPlayerGame flushWaitingRoom(){
        MultiPlayerGame game = new MultiPlayerGame(waitingRoomId, players, questions);
        questions = new ArrayList<>();
        generateNewQuestions();
        players = new ArrayList<>();
        waitingRoomId++;
        return game;
    }



}
