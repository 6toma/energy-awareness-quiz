package server.api;

import commons.ChangesMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/poll")
public class LongPollController {

    /**
     * List of everything thats different from last poll
     * For each thing in list we send another request for the body of that change
     */
    private static final List<ChangesMessage> changesMessageList = new ArrayList<>();

    /**
    private MultiplayerGameObject multiplayerGame;

    public LongPollController(MultiplayerGameObject multiplayerGame){
        this.multiplayerGame = multiplayerGame;
    }
     */


    /**
     * Cool piece of code found, it redirects so it keeps polling
     * @param input
     * @return
     * @throws InterruptedException
     */
    private ResponseEntity<List<ChangesMessage>> keepPolling(ChangesMessage input) throws InterruptedException {
        Thread.sleep(5000);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/getMessages?id=" + input.getId() + "&type=" + input.getServerChangeType()));
        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }

}
