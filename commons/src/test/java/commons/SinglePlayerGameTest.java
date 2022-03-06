package commons;

import org.junit.jupiter.api.Assertions;
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
        assertEquals(1066, singlePlayerGame.getPlayerScore());
    }

    @Test
    public void TestAddPointsWrongAnswer() {
        int scoreBefore = singlePlayerGame.getPlayerScore();
        singlePlayerGame.addPoints(2137, 1.0);
        assertEquals(scoreBefore, singlePlayerGame.getPlayerScore());
    }
    @Test
    public void TestAddPointsGuessQuestion() {
        singlePlayerGame.setStreak(3); // once the question is answered the streak is incremented and the score calculated (actual streak will be 4)
        singlePlayerGame.addPoints(7, 0.49);
        assertEquals(517, singlePlayerGame.getPlayerScore());
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







}
