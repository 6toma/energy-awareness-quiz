package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Activity class
 * Activities are stored in the database
 */
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private Integer consumption_in_wh;
    private String source;

    public Activity(){ } // needed for creating object from JSON

    public Activity(String title, Integer consumption_in_wh, String source) {
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    public Activity(Long id, String title, Integer consumption_in_wh, String source) {
        this.id = id;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    @Override
    public String toString() {
        return "Activity{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", consumption_in_wh=" + consumption_in_wh +
            ", source='" + source + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id) && Objects.equals(title, activity.title) && Objects.equals(consumption_in_wh, activity.consumption_in_wh) && Objects.equals(source, activity.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, consumption_in_wh, source);
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

    public Integer getConsumption_in_wh() {
        return consumption_in_wh;
    }

    public void setConsumption_in_wh(Integer consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
