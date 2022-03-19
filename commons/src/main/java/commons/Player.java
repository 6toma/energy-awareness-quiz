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


    public Player(){ } // needed for creating object from JSON

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public Player(String name, Integer score) {
        this.name = name;
        this.score = score >= 0 ? score : 0; // check if score is smaller than 0
    }

    public Player(Long id, String name, Integer score) {
        this.id = id;
        this.name = name;
        this.score = score >= 0 ? score : 0; // check if score is smaller than 0
    }
}
