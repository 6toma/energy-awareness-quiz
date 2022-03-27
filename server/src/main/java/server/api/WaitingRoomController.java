package server.api;

import commons.questions.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Endpoints and methods for the waiting
 */

@RestController
@RequestMapping("/api/waitingroom")
public class WaitingRoomController {

    /**
     *
     */
    public WaitingRoomController() {};

    /**
     * Endpoint for checking whether a waiting room is created
     */
    @GetMapping(path = {"/exists", "/exists/"})
    public ResponseEntity<Boolean> doesWaitingRoomExist() {
        return ResponseEntity.ok(true);
    }

    /**
     * generating a new set of questions
     */
    private List<Question> generateNewQuestions(int maxNumberOfQuestions) {
        List<Question> result = new ArrayList<>();
        int count = maxNumberOfQuestions;
        while (count > 0){
            //if(result.add(getRandomQuestion()) { count--; }
        }
        return result;
    }

    /**
     * Endpoint for checking whether a player with a username already exists
     */
    @GetMapping(path = {"/username/{username}", "/username/{username}/"})
    public ResponseEntity<Boolean> doesUsernameExist(@PathVariable("username") String username) {
        return ResponseEntity.ok(true);
    }
}
