package com.sabanciuniv.it592.subot.subotbackend.service;

import com.sabanciuniv.it592.subot.subotbackend.model.Session;
import com.sabanciuniv.it592.subot.subotbackend.model.SessionItem;
import com.sabanciuniv.it592.subot.subotbackend.repository.SessionItemRepository;
import com.sabanciuniv.it592.subot.subotbackend.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessionService {

    private static String ENGINE_URL = System.getenv("SUBOT_ENGINE_URL"); // "http://192.168.1.103:5000";

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    SessionItemRepository sessionItemRepository;

    @Autowired
    RestTemplate restTemplate;

    public Session getOrCreateSession(Long id) {
        if (id != null) {
            if (!sessionRepository.existsById(id)) {
                Session session = new Session();
                session.setId(id);
                session.setCreatedAt(LocalDateTime.now());
                return sessionRepository.save(session);
            }
        } else {
            return sessionRepository.getById(id);
        }
        return null;
    }

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public Session createNewSession() {
        Session session = new Session();
        session.setCreatedAt(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    public void deleteSession(Session session) {
        sessionRepository.delete(session);
    }

    public SessionItem createNewSessionItem(Long sessionId, SessionItem item) {

        if (sessionRepository.existsById(sessionId)) {
            item.setSession(sessionRepository.getById(sessionId));
        }

        return sessionItemRepository.save(item);
    }

    public String askQuestion(String question) {
        String url = ENGINE_URL + "/?question=" + question;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

}
