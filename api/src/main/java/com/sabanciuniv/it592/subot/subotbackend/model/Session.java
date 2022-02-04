package com.sabanciuniv.it592.subot.subotbackend.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue
    private Long id;

    private String hostname;

    @OneToMany(mappedBy = "session")
    private List<SessionItem> sessionItems = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime closedAt;
    private boolean isExpired = false;

    public Session(String hostname) {
        this.hostname = hostname;
    }

}
