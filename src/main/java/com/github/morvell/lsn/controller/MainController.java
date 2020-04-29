package com.github.morvell.lsn.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.morvell.lsn.domain.User;
import com.github.morvell.lsn.domain.Views;
import com.github.morvell.lsn.dto.MessagePageDto;
import com.github.morvell.lsn.service.MessageService;

/**
 * @author Андрей Захаров
 * @created 19/04/2020
 */
@Controller
@RequestMapping("/")
public class MainController {

    private final MessageService messageService;

    @Value("${spring.profiles.active}")
    private String profile;
    private final ObjectWriter writer;

    public MainController(MessageService messageService, ObjectMapper mapper) {

        this.messageService = messageService;

        this.writer = mapper.setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.FullMessage.class);
    }


    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user)
            throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();

        if (user != null) {
            data.put("profile", user);

            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            PageRequest pageRequest = PageRequest.of(0, MessageController.MESSAGES_PER_PAGE, sort);
            MessagePageDto messagePageDto = messageService.findAll(pageRequest);

            String messages = writer.writeValueAsString(messagePageDto.getMessages());

            model.addAttribute("messages", messages);
            data.put("currentPage", messagePageDto.getCurrentPage());
            data.put("totalPages", messagePageDto.getTotalPages());
        } else {
            model.addAttribute("messages", "[]");
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }

}
