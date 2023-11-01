package ru.job4j.shortcut.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String code;

    @ManyToOne
    @JoinColumn(name = "site_id", foreignKey = @ForeignKey(name = "SITE_ID_FK"))
    private Site site;

    @Column(name = "call_number")
    private long callNumber;

}