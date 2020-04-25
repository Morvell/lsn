package com.github.morvell.lsn.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.morvell.lsn.domain.Views;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Андрей Захаров
 * @created 22/04/2020
 */
@Data
@AllArgsConstructor
@JsonView(Views.Id.class)
public class WsEventDto implements Serializable {

    private ObjectType objectType;

    private EventType eventType;

    @JsonRawValue
    private String body;
}
