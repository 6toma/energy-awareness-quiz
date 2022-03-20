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

    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public Activity() {
    }

    /**
     * Constructor with all parameters except ID
     * @param image_path path of the image
     * @param title Activity text used in questions
     * @param consumption_in_wh Consumption in Wh
     * @param source Source of the activity consumption
     */
    public Activity(String image_path, String title, Long consumption_in_wh, String source) {
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    /**
     * Constructor with all parameters
     * @param id ID of the activity in the database
     * @param image_path path of the image
     * @param title Activity text used in questions
     * @param consumption_in_wh Consumption in Wh
     * @param source Source of the activity consumption
     */
    public Activity(String id, String image_path, String title, Long consumption_in_wh, String source) {
        this.id = id;
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }
}
