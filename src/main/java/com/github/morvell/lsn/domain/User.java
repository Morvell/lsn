package com.github.morvell.lsn.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author Андрей Захаров
 * @created 05/04/2020
 */
@Table(name = "usr")
@Entity
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -324181049851953657L;

    @Id
    private String id;

    private String name;

    private String userpic;

    private String email;

    private String gender;

    private String locale;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastVisit;
}
