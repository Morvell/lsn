package com.github.morvell.lsn.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
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
import com.github.morvell.lsn.repo.MessageRepository;

/**
 * @author Андрей Захаров
 * @created 19/04/2020
 */
@Controller
@RequestMapping("/")
public class MainController {

    private final MessageRepository messageRepository;

    @Value("${spring.profiles.active}")
    private String profile;

    private final ObjectWriter writer;

    public MainController(MessageRepository messageRepository, ObjectMapper mapper) {

        this.messageRepository = messageRepository;
        writer = mapper.setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.FullMessage.class);
    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user)
            throws JsonProcessingException {

        HashMap<Object, Object> data = new HashMap<>();

        if (user != null) {
            data.put("profile", user);

            String messages = writer.writeValueAsString(messageRepository.findAll());
            model.addAttribute("messages", messages);
        } else {
            model.addAttribute("messages", "[]");
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }

}
