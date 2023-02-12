package com.bbb.votes.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

class Vote {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@RequestMapping("/vote")
@RestController
public class VoteController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @PostMapping
    String createVote(@RequestBody Vote vote) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Fui testado");
        String serializedVote;
        try {
            serializedVote = objectMapper.writeValueAsString(vote);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send("votes", serializedVote);
        return "oi";
    }
}
