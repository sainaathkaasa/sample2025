package com.coldchain.coldchain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userpassword_master")
public class UserPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private String id;

    @JsonIgnore
    private String password;

    @OneToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;


}
