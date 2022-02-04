package com.sabanciuniv.it592.subot.subotbackend.controller;

import com.sabanciuniv.it592.subot.subotbackend.beans.SessionItemDTO;
import com.sabanciuniv.it592.subot.subotbackend.model.Session;
import com.sabanciuniv.it592.subot.subotbackend.model.SessionItem;
import com.sabanciuniv.it592.subot.subotbackend.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController("/session")
public class SessionController {

    @Autowired
    SessionService sessionService;

    @GetMapping("/get")
    public ResponseEntity<Session> getSessionsById(@RequestParam String id) {
        Optional<Session> session = sessionService.getSessionById(Long.valueOf(id));
        if (session.isPresent()) {
            return new ResponseEntity<>(session.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteSessionById(@RequestParam String id) {
        Optional<Session> session = sessionService.getSessionById(Long.valueOf(id));
        if (session.isPresent()) {
            sessionService.deleteSession(session.get());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Session> createNewSession() {
        Session session = sessionService.createNewSession();
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @PostMapping(value = "/ask")
    public ResponseEntity<SessionItem> askQuestion(@RequestBody SessionItemDTO body) {
        if (!sessionService.getSessionById(body.getSessionId()).isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // Store incoming session item
        SessionItem sessionItem = sessionService.createNewSessionItem(body.getSessionId(), new SessionItem(body.getMessage()));
        sessionItem.setAgent(false);
        sessionService.createNewSessionItem(body.getSessionId(), sessionItem);

        // Fetch answer from engine
        String answer = sessionService.askQuestion(body.getMessage());

        // Store engine's response
        SessionItem answerSessionItem = new SessionItem(answer);
        answerSessionItem.setAgent(true);
        sessionService.createNewSessionItem(body.getSessionId(), answerSessionItem);

        return new ResponseEntity<>(answerSessionItem, HttpStatus.OK);
    }
}
