package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Activity class that is stored in the database
 *
 * Stores an activities
 */
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String name;
    private Integer score;

    public Player(){ } // needed for creating object from JSON

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public Player(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) && Objects.equals(score, player.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Integer getScore() {return score;}

    public void setScore(Integer score) {this.score = score;}
}
