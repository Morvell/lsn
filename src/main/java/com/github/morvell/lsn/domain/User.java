package com.github.morvell.lsn.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Андрей Захаров
 * @created 05/04/2020
 */
@Table(name = "usr")
@Entity
@Data
public class User {

    @Id
    private String id;

    private String name;

    private String userpic;

    private String email;

    private String gender;

    private String locale;

    private LocalDateTime lastVisit;
}
