package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private Integer consumption;
    //private String source;


    public Activity(String title, Integer consumption) {
        this.title = title;
        this.consumption = consumption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getConsumption() {
        return consumption;
    }

    public void setConsumption(Integer consumption) {
        this.consumption = consumption;
    }

    @Override
    public String toString() {
        return "Activity{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", consumption=" + consumption +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id) && Objects.equals(title, activity.title) && Objects.equals(consumption, activity.consumption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, consumption);
    }
}
