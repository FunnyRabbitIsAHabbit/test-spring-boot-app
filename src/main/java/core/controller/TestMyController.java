package core.controller;

import core.model.SomeResponce;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestMyController {

    @GetMapping("/hello")
    public SomeResponce sayHelloToUser(@RequestBody String name) {
        SomeResponce responce = new SomeResponce();
        responce.setResponse("Hello, " + name);

        return responce;
    }

    @RequestMapping(value = "/q/{name}", method = RequestMethod.GET)
    public SomeResponce variableSayHelloToUser(@PathVariable String name,
                                               @RequestParam(value = "v",
                                               required = false) Integer condition) {
        SomeResponce responce = new SomeResponce();
        condition = condition == null ? 2 : condition;
        switch (condition) {
            case 0 -> responce.setResponse("Hello, " + name);
            case 1 -> responce.setResponse("Hello, how are you? " + name);
            default -> responce.setResponse("Hello, this is not allowed, " + name);
        }


        return responce;
    }
}
