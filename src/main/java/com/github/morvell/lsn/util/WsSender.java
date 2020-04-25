package com.github.morvell.lsn.util;

import java.util.function.BiConsumer;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.morvell.lsn.dto.EventType;
import com.github.morvell.lsn.dto.ObjectType;
import com.github.morvell.lsn.dto.WsEventDto;

/**
 * @author Андрей Захаров
 * @created 22/04/2020
 */
@Component
public class WsSender {

    private final SimpMessagingTemplate template;

    private final ObjectMapper mapper;

    public WsSender(SimpMessagingTemplate template, ObjectMapper mapper) {

        this.template = template;
        this.mapper = mapper;
    }

    public <T> BiConsumer<EventType, T> getSender(ObjectType objectType, Class view) {

        var writer = mapper.setConfig(mapper.getSerializationConfig()).writerWithView(view);

        return (EventType eventType, T payload) -> {
            try {
                template.convertAndSend("/topic/activity",
                        new WsEventDto(objectType, eventType, writer.writeValueAsString(payload)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
