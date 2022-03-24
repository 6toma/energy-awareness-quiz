package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ChangesMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;
    @Setter
    @Getter
    private Integer serverChangeType;

    /**
     * Maybe some json in the future
     */
    public ChangesMessage(){ }

    public ChangesMessage(int changeType){
        this.serverChangeType = changeType;
    }

}
