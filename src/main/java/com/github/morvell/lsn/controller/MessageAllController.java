package com.github.morvell.lsn.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.morvell.lsn.domain.User;
import com.github.morvell.lsn.domain.Views;
import com.github.morvell.lsn.dto.MessagePageDto;
import com.github.morvell.lsn.service.MessageService;

/**
 * @author Андрей Захаров
 * @created 03/05/2020
 */
@RestController
@RequestMapping("allMessage")
public class MessageAllController {

    public static final int MESSAGES_PER_PAGE = 3;

    private final MessageService messageService;

    public MessageAllController(MessageService messageService) {

        this.messageService = messageService;
    }

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public MessagePageDto list(@PageableDefault(size = MESSAGES_PER_PAGE,
            sort = { "id" },
            direction = Sort.Direction.DESC) Pageable pageable) {

        return messageService.findAll(pageable);
    }
}
