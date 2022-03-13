package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Activity class
 * Activities are stored in the database
 */
@Entity
public class Activity {

    /*
    @GeneratedValue(strategy = GenerationType.AUTO)
    */
    @Id
    private String id;

    private String image_path;
    private String title;
    private Long consumption_in_wh;
    private String source;

    public Activity(){ } // needed for creating object from JSON

    /*
    public Activity(String image_path, String title, Integer consumption_in_wh, String source) {
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

     */

    public Activity(String id, String image_path, String title, Long consumption_in_wh, String source) {
        this.id = id;
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", image_path='" + image_path + '\'' +
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
        return Objects.equals(id, activity.id) && Objects.equals(image_path, activity.image_path) && Objects.equals(title, activity.title) && Objects.equals(consumption_in_wh, activity.consumption_in_wh) && Objects.equals(source, activity.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, consumption_in_wh, source);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getConsumption_in_wh() {
        return consumption_in_wh;
    }

    public void setConsumption_in_wh(Long consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
