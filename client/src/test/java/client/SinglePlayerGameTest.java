package client;

import client.SinglePlayerGame;
import commons.Player;
import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SinglePlayerGameTest {

    SinglePlayerGame singlePlayerGame;

    @BeforeEach
    public void setup(){
        List<Question> questions = new ArrayList<>();
        singlePlayerGame = new SinglePlayerGame(new Player("a"), questions);
    }

    @Test
    public void TestConstructor() {
        assertNotNull(singlePlayerGame);
    }

    @Test
    public void TestAddQuestion(){
        singlePlayerGame.addQuestion(new Question());
        assertEquals(1, singlePlayerGame.getQuestions().size());
    }

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

    @Test
    public void TestAddPointsRegularQuestion() {
        singlePlayerGame.setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        singlePlayerGame.addPoints(5, 1.0);
        assertEquals(1066, singlePlayerGame.getPlayer().getScore());
    }

    @Test
    public void TestAddPointsWrongAnswer() {
        int scoreBefore = singlePlayerGame.getPlayer().getScore();
        singlePlayerGame.addPoints(-1, 1.0);
        assertEquals(scoreBefore, singlePlayerGame.getPlayer().getScore());
    }
    @Test
    public void TestAddPointsGuessQuestion() {
        singlePlayerGame.setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        singlePlayerGame.addPoints(7, 0.49);
        assertEquals(517, singlePlayerGame.getPlayer().getScore());
    }

    @Test
    public void TestResetSteak() {
        singlePlayerGame.setStreak(5);
        singlePlayerGame.resetStreak();
        assertEquals(0, singlePlayerGame.getStreak());
    }
    @Test
    public void TestNextQuestion() {
        for(int i = 0; i < 5; i++){
            singlePlayerGame.nextQuestion();
        }
        assertEquals(6, singlePlayerGame.getQuestionNumber());
    }
    @Test
    public void TestStreakIncrement() {
        for(int i = 0; i < 6; i++){
            singlePlayerGame.incrementStreak();
        }
        assertEquals(6, singlePlayerGame.getStreak());
    }
    
    @Test
    public void TestStreakAfterWrongAnswer() {
        singlePlayerGame.addPoints(-1, 1.0);
        assertEquals(0, singlePlayerGame.getStreak());
    }
    @Test
    public void TestPlayerGetter() {
        Player p = new Player("a", 420);
        singlePlayerGame.getPlayer().setScore(420);
        assertEquals(p, singlePlayerGame.getPlayer());
    }

    @Test
    public void TestStreakGetter() {
        assertEquals(0, singlePlayerGame.getStreak());
        singlePlayerGame.setStreak(3);
        assertEquals(3, singlePlayerGame.getStreak());
    }

    @Test
    public void TestQuestionGetter() {
        singlePlayerGame.addQuestion(new Question());
        singlePlayerGame.addQuestion(new Question());
        assertEquals(2, singlePlayerGame.getQuestions().size());
    }

    @Test
    public void TestQuestionGetterNotNull() {
        singlePlayerGame.addQuestion(new Question());
        singlePlayerGame.addQuestion(new Question());
        assertNotNull(singlePlayerGame.getQuestions().get(0));
    }

    @Test
    public void TestQuestionSetter() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question());
        questions.add(new Question());
        singlePlayerGame.setQuestions(questions);
        assertEquals(2, singlePlayerGame.getQuestions().size());
    }

    @Test
    public void TestQuestionNumberGetter() {
        singlePlayerGame.nextQuestion();
        singlePlayerGame.nextQuestion();
        singlePlayerGame.nextQuestion();
        assertEquals(4, singlePlayerGame.getQuestionNumber());
    }

    @Test
    public void TestPlayerSetter() {
        Player p = new Player("b", 420);
        singlePlayerGame.setPlayer(p);
        assertEquals(p, singlePlayerGame.getPlayer());
    }

    @Test
    public void TestStreakSetter() {
        singlePlayerGame.setStreak(3);
        assertEquals(3, singlePlayerGame.getStreak());
    }

    @Test
    public void TestQuestionNumberSetter() {
        singlePlayerGame.setQuestionNumber(3);
        assertEquals(3, singlePlayerGame.getQuestionNumber());
    }
}
