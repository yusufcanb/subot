package com.sabanciuniv.it592.subot.subotbackend.repository;

import com.sabanciuniv.it592.subot.subotbackend.model.SessionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionItemRepository extends JpaRepository<SessionItem, Long> {
}
