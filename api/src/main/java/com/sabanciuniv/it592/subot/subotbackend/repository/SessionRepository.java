package com.sabanciuniv.it592.subot.subotbackend.repository;

import com.sabanciuniv.it592.subot.subotbackend.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
