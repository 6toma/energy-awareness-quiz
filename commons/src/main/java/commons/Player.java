package commons;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Activity class that is stored in the database
 *
 * Stores an activities
 */
@ToString @EqualsAndHashCode
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;

    @Getter
    private String name;
    @Getter
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

    public void setName(String name) {
        this.name = name;
        if(name.length() > 0)
            this.name = name;
        else throw new IllegalArgumentException("Name cannot be empty");
    }

    public void setScore(Integer score) {
        this.score = score;
        if(score >= 0)
            this.score = score;
        else throw new IllegalArgumentException("Score cannot be negative");
    }
}
