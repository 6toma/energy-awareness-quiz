package commons;

import lombok.Data;

import java.util.List;

/**
 * Multiplayer game class
 * Handles a list of players instead of just one
 */
@Data
public class MultiPlayerGame {

    private List<Question> questions;
    private List<Player> players;

}
