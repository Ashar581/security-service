package com.security.service.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sos")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class SOS {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sosId;
    private Set<String> sosContacts;
    private String sosMessage;
    @JsonBackReference
    @OneToOne(mappedBy = "sosContact")
    private User user;
}
