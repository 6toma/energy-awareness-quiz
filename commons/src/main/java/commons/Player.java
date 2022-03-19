package commons;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Activity class that is stored in the database
 *
 * Stores an activities
 */
@Data
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer score;

    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public Player(){ }

    /**
     * Constructor which specifies the name
     * @param name Name of the player
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    /**
     * Constructor which specifies the name and the score
     * @param name Name of the player
     * @param score Player score
     */
    public Player(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Constructor with all parameters
     * @param id ID of the player in the database
     * @param name Name of the player
     * @param score Player score
     */
    public Player(Long id, String name, Integer score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }
}
