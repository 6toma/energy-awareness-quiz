package client;

import commons.Activity;
import commons.ComparativeQuestion;
import commons.Player;
import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SinglePlayerGameTest {

    SinglePlayerGame singlePlayerGame;

    /**
     * Creates a new singleplayergame with an empty list of questions
     * Run before each test
     */
    @BeforeEach
    public void setup(){
        List<Question> questions = new ArrayList<>();
        singlePlayerGame = new SinglePlayerGame(new Player("a"), questions);
    }

    /**
     * Tests if the empty constructor creates an object
     */
    @Test
    public void TestEmptyConstructor() {
        singlePlayerGame = new SinglePlayerGame();
        assertNotNull(singlePlayerGame);
    }

    /**
     * Tests if the constructor with Player creates a new object
     */
    @Test
    public void TestConstructorPlayer() {
        singlePlayerGame = new SinglePlayerGame(new Player("a"));
        assertNotNull(singlePlayerGame);
    }

    /**
     * Tests if the constructor with maxQuestions creates a new object
     */
    @Test
    public void TestConstructorMaxQuestions() {
        singlePlayerGame = new SinglePlayerGame(1);
        assertNotNull(singlePlayerGame);
    }

    /**
     * Tests if the constructor with maxQuestions and username creates a new object
     */
    @Test
    public void TestConstructorMaxQuestionsUserName() {
        singlePlayerGame = new SinglePlayerGame(1, "username");
        assertNotNull(singlePlayerGame);
    }

    /**
     * Tests if the constructor in setup creates a new object
     */
    @Test
    public void TestConstructor() {
        assertNotNull(singlePlayerGame);
    }

    /**
     * Tests if a question is added successfully
     */
    @Test
    public void TestAddQuestion(){
        singlePlayerGame.addQuestion(new ComparativeQuestion());
        assertEquals(1, singlePlayerGame.getQuestions().size());
    }

    /**
     * Tests if the points to be added calculation is correct
     */
    @Test
    public void TestGetPointsToBeAdded() {
        singlePlayerGame.setStreak(5);
        assertEquals(1050, singlePlayerGame.getPointsToBeAdded(10));
        singlePlayerGame.setStreak(1);
        assertEquals(1010, singlePlayerGame.getPointsToBeAdded(10));
        assertEquals(1015, singlePlayerGame.getPointsToBeAdded(9));
        singlePlayerGame.setStreak(3);
        assertEquals(1056, singlePlayerGame.getPointsToBeAdded(5));
    }

    /**
     * Tests if the points to be added for a regular question is correct
     */
    @Test
    public void TestAddPointsRegularQuestion() {
        singlePlayerGame.setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        singlePlayerGame.addPoints(5, 1.0);
        assertEquals(1066, singlePlayerGame.getPlayer().getScore());
    }

    /**
     * Tests if the points to be added for a wrong answer is correct
     */
    @Test
    public void TestAddPointsWrongAnswer() {
        int scoreBefore = singlePlayerGame.getPlayer().getScore();
        singlePlayerGame.addPoints(-1, 1.0);
        assertEquals(scoreBefore, singlePlayerGame.getPlayer().getScore());
    }

    /**
     * Tests if the points to be added for an estimation question is correct
     */
    @Test
    public void TestAddPointsGuessQuestion() {
        singlePlayerGame.setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        singlePlayerGame.addPoints(7, 0.49);
        assertEquals(517, singlePlayerGame.getPlayer().getScore());
    }

    /**
     * Tests the resetStreak method
     */
    @Test
    public void TestResetSteak() {
        singlePlayerGame.setStreak(5);
        singlePlayerGame.resetStreak();
        assertEquals(0, singlePlayerGame.getStreak());
    }

    /**
     * Tests the nextQuestion method
     */
    @Test
    public void TestNextQuestion() {
        for(int i = 0; i < 5; i++){
            singlePlayerGame.nextQuestion();
        }
        assertEquals(6, singlePlayerGame.getQuestionNumber());
    }

    /**
     * Tests the incrementStreak method
     */
    @Test
    public void TestStreakIncrement() {
        for(int i = 0; i < 6; i++){
            singlePlayerGame.incrementStreak();
        }
        assertEquals(6, singlePlayerGame.getStreak());
    }

    /**
     * Tests if the streak is reset after a wrong answer
     */
    @Test
    public void TestStreakAfterWrongAnswer() {
        singlePlayerGame.addPoints(-1, 1.0);
        assertEquals(0, singlePlayerGame.getStreak());
    }

    /**
     * Test for getPlayer
     */
    @Test
    public void TestPlayerGetter() {
        Player p = new Player("a", 420);
        singlePlayerGame.getPlayer().setScore(420);
        assertEquals(p, singlePlayerGame.getPlayer());
    }

    /**
     * Test for getStreak
     */
    @Test
    public void TestStreakGetter() {
        assertEquals(0, singlePlayerGame.getStreak());
        singlePlayerGame.setStreak(3);
        assertEquals(3, singlePlayerGame.getStreak());
    }

    /**
     * Tests if question gets added
     */
    @Test
    public void TestQuestionGetter() {
        singlePlayerGame.addQuestion(new ComparativeQuestion());
        singlePlayerGame.addQuestion(new ComparativeQuestion(new ArrayList<Activity>(), true));
        assertEquals(2, singlePlayerGame.getQuestions().size());
    }

    /**
     * Test if question added was not null
     */
    @Test
    public void TestQuestionGetterNotNull() {
        singlePlayerGame.addQuestion(new ComparativeQuestion());
        singlePlayerGame.addQuestion(new ComparativeQuestion());
        assertNotNull(singlePlayerGame.getQuestions().get(0));
    }

    /**
     * Test for setQuestion
     */
    @Test
    public void TestQuestionSetter() {
        List<Question> questions = new ArrayList<>();
        questions.add(new ComparativeQuestion());
        questions.add(new ComparativeQuestion());
        singlePlayerGame.setQuestions(questions);
        assertEquals(2, singlePlayerGame.getQuestions().size());
    }

    /**
     * Test for getQuestionNumber
     */
    @Test
    public void TestQuestionNumberGetter() {
        singlePlayerGame.nextQuestion();
        singlePlayerGame.nextQuestion();
        singlePlayerGame.nextQuestion();
        assertEquals(4, singlePlayerGame.getQuestionNumber());
    }

    /**
     * Test for setPlayer
     */
    @Test
    public void TestPlayerSetter() {
        Player p = new Player("b", 420);
        singlePlayerGame.setPlayer(p);
        assertEquals(p, singlePlayerGame.getPlayer());
    }

    /**
     * Test for setStreak
     */
    @Test
    public void TestStreakSetter() {
        singlePlayerGame.setStreak(3);
        assertEquals(3, singlePlayerGame.getStreak());
    }

    /**
     * Test for setQuestionNumber
     */
    @Test
    public void TestQuestionNumberSetter() {
        singlePlayerGame.setQuestionNumber(3);
        assertEquals(3, singlePlayerGame.getQuestionNumber());
    }
}
