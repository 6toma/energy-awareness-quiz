package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComparativeQuestionTest {

    List<Activity> activities;

    @BeforeEach
    public void setup(){
        activities = List.of(
            new Activity(1L,"a", 1, "a"),
            new Activity(2L,"b", 2, "b"),
            new Activity(3L,"c", 3, "c")
        );
    }

    @Test
    void generateCorrectAnswerMost() {
        ComparativeQuestion q = new ComparativeQuestion(activities, true);
        assertEquals(2, q.getCorrect_answer());
    }

    @Test
    void generateCorrectAnswerLeast() {
        ComparativeQuestion q = new ComparativeQuestion(activities, false);
        assertEquals(0, q.getCorrect_answer());
    }

    @Test
    void generateCorrectAnswerNull() {
        ComparativeQuestion q = new ComparativeQuestion(null, true);
        assertEquals(-1, q.getCorrect_answer());
    }

    @Test
    void generateCorrectAnswerEmpty() {
        ComparativeQuestion q = new ComparativeQuestion(new ArrayList<Activity>(), true);
        assertEquals(-1, q.getCorrect_answer());
    }

    @Test
    void generateCorrectAnswerOne() {
        ComparativeQuestion q = new ComparativeQuestion(List.of(activities.get(0)), true);
        assertEquals(0, q.getCorrect_answer());
    }
}