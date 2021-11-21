package ru.vorobyov.voting.controller;


import org.springframework.web.bind.annotation.*;
import ru.vorobyov.voting.controller.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("message") // all request sort of /site/message go to this controller
public class MessageController {
    private int counter = 4;
    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
        add(new HashMap<String, String>() {{ put("id", "1"); put("text", "First Message"); }});
        add(new HashMap<String, String>() {{ put("id", "2"); put("text", "Second Message"); }});
        add(new HashMap<String, String>() {{ put("id", "3"); put("text", "Third Message"); }});
    }};

    @GetMapping
    public List<Map<String, String>> list(){
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id){
        return getMessage(id);
    }

    private Map<String, String> getMessage(String id) {
        return messages.stream().
                filter(message -> message.get("id").equals(id)).
                findFirst().orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message){
        message.put("id", String.valueOf(counter++));

        messages.add(message);

        return message;
    }

    @PutMapping
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message){
        Map<String, String> messageFromDb = getMessage(message.get("id"));

        messageFromDb.putAll(message);
        messageFromDb.put("id", id);

        return messageFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@RequestBody String id){
        Map<String, String> message = getMessage(id);

        messages.remove(message);
    }


}
