package commons.questions;

import commons.Activity;
import lombok.Data;

import java.util.List;
import java.util.Random;

@Data
/**
 * Class for MC Question. A player is supposed to choose how much energy does an activity waste
 */
public class MCQuestion {
    private Activity activity;

    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public MCQuestion() {
    }

    /**
     * constructor with an activity
     *
     * @param activities to add to question
     */
    public MCQuestion(Activity activity) {
        this.activity = activity;
    }
}
