package com.events.eventPlanner.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.sql.Date;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq_gen")
    @SequenceGenerator(name="user_id_seq_gen", sequenceName = "users_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "login")
    @Pattern(regexp = "[A-z]*")
    private String login;

    @Column(name = "password")
    @Size(min = 8, max = 16)
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "created")
    private Date created;

    @Column(name = "edited")
    private Date edited;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "birthday_date")
    @Past
    private Date birthdate;

    @Column(name = "email")
    @Email
    private String email;
}
