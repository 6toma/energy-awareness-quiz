package commons;

import lombok.Data;
import lombok.ToString;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
    @ToString.Exclude // Image is not included in the toString method (would make it too long)
    @Transient // Image is not stored in the database
    private byte[] image; // image is a byte array because it's a pretty efficient way to send it (I hope)

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
        try{
            // Reads the image from the file to a BufferedImage
            BufferedImage img = ImageIO.read(new File("./server/src/main/resources/activity-bank-pictures/" + this.image_path));
            // Creates a new ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // Writes the image to the output stream. Sending the image gives an error (because it's inefficient and shouldn't be done)
            ImageIO.write(img, "png", outputStream); // jpg would be faster but png supports transparency
            this.image = outputStream.toByteArray();
        } catch (IOException | IllegalArgumentException e){ // Catches an error if an image couldn't be found
            System.err.println("Couldn't find picture");
            this.image = null;
        }
    }
}
