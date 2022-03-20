package commons;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Activity class
 * Activities are stored in the database
 */
@Data
@Entity
public class Activity {
    
    @Id
    private String id;

    private String image_path;
    private String title;
    private Long consumption_in_wh;
    private String source;

    // needed for creating object from JSON
    public Activity() {
    }


    public Activity(String image_path, String title, Long consumption_in_wh, String source) {
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    public Activity(String id, String image_path, String title, Long consumption_in_wh, String source) {
        this.id = id;
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }
}
