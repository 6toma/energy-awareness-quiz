package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Example server controller
 */
@Controller
@RequestMapping("/") // These tags are needed for Spring to know this is a controller
public class SomeController {

    @GetMapping("/") // Execute index() on HTTP GET with path "/"
    @ResponseBody
    public String index() {
        return "Hello world!";
    }
}