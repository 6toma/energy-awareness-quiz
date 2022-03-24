package server.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


@RestController
@RequestMapping("/api/poll")
public class LongPollController {

    /**
     * List of everything thats different from last poll
     * For each thing in list we send another request for the body of that change
     */
    private static final List<Integer> changesMessageList = new ArrayList<>();

    /**
    private MultiplayerGameObject multiplayerGame;

    public LongPollController(MultiplayerGameObject multiplayerGame){
        this.multiplayerGame = multiplayerGame;
    }
     */

    @GetMapping("CurrentScreen")
    public ResponseEntity<String> getCurrentScreen(){
        return ResponseEntity.ok(null)
    }

    private Map<Object, Consumer<Integer>> listeners = new HashMap<>();

    /**
     * Gets a number that corresponds to what has changed
     * @return
     */
    @GetMapping("/update")
    public DeferredResult<ResponseEntity<Integer>> getUpdate(){
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Integer>>(5000L,noContent);
        var key = new Object();
        listeners.put(key,c ->{
            res.setResult(ResponseEntity.ok(c));
        });
        res.onCompletion(()-> {
            listeners.remove(key);
        });
        return res;
    }

    /**
     * Returns a number that's 2^x which describes what is
     * different compared to what the client sent
     * so the client can request for those specific objects
     * instead of the whole game object
     * @param change
     * @return
     */
    @PostMapping(path = {"add-update"})
    public ResponseEntity<Integer> giveUpdate(Integer change){
        listeners.forEach((k,l) -> l.accept(change));
        return ResponseEntity.ok(change);
    }

    /**
     * Cool piece of code found, it redirects so it keeps polling
     * obsolete right now but could be used in the future
     * @param input
     * @return
     * @throws InterruptedException
     */
    private ResponseEntity<List<Integer>> keepPolling(Integer input) throws InterruptedException {
        Thread.sleep(5000);
        HttpHeaders headers = new HttpHeaders();
        //"/getMessages?id=" + input.getID() + "&type=" + input.getType()
        headers.setLocation(URI.create("/getMessages?id=" + input + "&type=" + input));
        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }

}
