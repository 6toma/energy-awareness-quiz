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

    @Override
    public String toString() {
        if(name != null)
            return "Player{" +
                    "ID=" + id +
                    ", name='" + name + '\'' +  // difference is on this line
                    ", score=" + score +
                    '}';
        return "Player{" +
                "ID=" + id +
                ", name=null" + // difference is because non-null name has ' ' marks before and after, but a null name shouldn't have them
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) && Objects.equals(score, player.score) && Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, score);
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        if(name.length() > 0)
            this.name = name;
        else throw new IllegalArgumentException("Name cannot be empty");
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        if(score >= 0)
            this.score = score;
        else throw new IllegalArgumentException("Score cannot be negative");
    }
}
