package com.security.service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_id")
    private Long profileId;
    private String profileName;
    private String profileType;
    private byte [] data;
    @OneToOne(mappedBy = "profile")
    private User user;
}
