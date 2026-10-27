package com.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users", schema = "chathub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount {

    @Id
    private UUID id;

    @Column(name = "display_name",nullable = false,length = 80)
    private String displayName;

    @Column(nullable = false,unique = true,length = 254)
    private String email;

    @Column(nullable = false, unique = true, length = 100)
    private String passwordHash;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false,updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public UserAccount(String displayName, String email, String passwordHash){
        this.id=UUID.randomUUID();
        this.displayName=displayName;
        this.email=email.strip().toLowerCase(Locale.ROOT);
        this.passwordHash=passwordHash;
        this.enabled=true;
    }

    @PrePersist
    void onCreate(){
        var now=Instant.now();
        createdAt=now;
        updatedAt=now;
    }

    @PreUpdate
    void onUpdate(){
        updatedAt=Instant.now();
    }


}
