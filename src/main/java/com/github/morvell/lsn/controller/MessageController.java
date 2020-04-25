package com.github.morvell.lsn.controller;

import java.util.List;
import java.util.function.BiConsumer;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.morvell.lsn.domain.Message;
import com.github.morvell.lsn.domain.Views;
import com.github.morvell.lsn.dto.EventType;
import com.github.morvell.lsn.dto.ObjectType;
import com.github.morvell.lsn.repo.MessageRepository;
import com.github.morvell.lsn.util.WsSender;

@RestController
@RequestMapping("message")
public class MessageController {

    private final MessageRepository messageRepository;

    private final BiConsumer<EventType, Message> wsSender;

    public MessageController(MessageRepository messageRepository, WsSender sender) {

        this.messageRepository = messageRepository;
        this.wsSender = sender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @JsonView(Views.IdName.class)
    @GetMapping
    public List<Message> list() {

        return messageRepository.findAll();
    }

    @GetMapping("{id}")
    public Message getOne(@PathVariable("id") Message message) {

        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) {

        var save = messageRepository.save(message);
        wsSender.accept(EventType.CREATE, save);
        return save;
    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDb,
            @RequestBody Message message) {

        BeanUtils.copyProperties(message, messageFromDb, "id");

        var save = messageRepository.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, save);
        return save;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {

        messageRepository.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    // @MessageMapping("/changeMessage")
    // @SendTo("/topic/activity")
    // public Message change(Message message) {
    //
    // return messageRepository.save(message);
    // }
}
