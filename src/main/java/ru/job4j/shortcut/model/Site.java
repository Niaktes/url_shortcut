package ru.job4j.shortcut.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "sites")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String domain;

    @NotBlank(message = "Login must not be empty")
    private String login;

    @NotBlank(message = "Password must not be empty")
    private String password;

}