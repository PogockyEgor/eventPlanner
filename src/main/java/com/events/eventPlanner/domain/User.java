package com.events.eventPlanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq_gen")
    @SequenceGenerator(name = "user_id_seq_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "login")
    @Pattern(regexp = "[A-z]*")
    private String login;

    @Column(name = "password")
    //@Size(min = 8, max = 16)
    private String password;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "created", updatable = false)
    private Date created;

    @JsonIgnore
    @Column(name = "edited")
    private Date edited;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "birthday_date")
    @Past
    private Date birthdate;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "role")
    private String role;
}