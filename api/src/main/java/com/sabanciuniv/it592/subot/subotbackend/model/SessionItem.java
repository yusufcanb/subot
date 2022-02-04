package com.sabanciuniv.it592.subot.subotbackend.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class SessionItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @Getter(AccessLevel.NONE)
    private Session session;

    @Column(length = 1024)
    private String message;
    private boolean isAgent;

    private LocalDateTime timestamp;

    public SessionItem(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
