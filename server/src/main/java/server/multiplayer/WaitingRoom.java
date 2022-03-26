package server.multiplayer;

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
    public WaitingRoom(int waitingRoomId, int maxNumberOfQuestions){
        this.players = new ArrayList<>();
        this.questions = new ArrayList<>();
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
    private void generateNewQuestions() {
        //TODO Once the question types are implemented we can generate the questions properly
        int count = maxNumberOfQuestions;
        while (count > 0){
         //if(addQuestion(server.getCompQuestion())){ count-- }
        }
    }

    /**
     * Adding a player to a Waiting room
     * @param player player to be added
     */
    public void addToWaitingRoom(Player player){
        players.add(player);
    }

    /**
     * Flushing a WaitingRoom. This will transfer all the players to a waiting room class with a set of questions and a.
     */
    //TODO Make the class return a MultiplayerGame class once it is created
    public void flushWaitingRoom(){
        // MultiplayerGame game = new MultiplayerGame(waitingRoomId, players, questions);
        questions = new ArrayList<>();
        generateNewQuestions();
        players = new ArrayList<>();
        waitingRoomId++;
        // return game
    }



}
