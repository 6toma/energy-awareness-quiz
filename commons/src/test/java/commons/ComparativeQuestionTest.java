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
            new Activity("1", "image_a", "a", 1L, "a"),
            new Activity("2", "image_b","b", 2L, "b"),
            new Activity("3", "image_c","c", 3L, "c")
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