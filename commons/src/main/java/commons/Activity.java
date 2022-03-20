package commons;

import lombok.Data;
import lombok.ToString;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    @ToString.Exclude
    private Image image;

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

    /**
     * Initializes the image
     * Uses image_path to find the image
     * Sets image to null if no image was found
     */
    public void initializeImage(){
        BufferedImage img;
        try{
            img = ImageIO.read(new File("./server/src/main/resources/images/" + this.image_path));
            this.image = img;
        } catch (IOException e){
            e.printStackTrace();
            System.err.println("Couldn't find picture");
            this.image = null;
        }
    }
}
