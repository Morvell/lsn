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
import com.github.morvell.lsn.repo.UserDetailsRepository;
import com.github.morvell.lsn.service.MessageService;

/**
 * @author Андрей Захаров
 * @created 19/04/2020
 */
@Controller
@RequestMapping("/")
public class MainController {

    private final MessageService messageService;

    private final UserDetailsRepository userDetailsRepo;

    @Value("${spring.profiles.active:prod}")
    private String profile;

    private final ObjectWriter messageWriter;

    private final ObjectWriter profileWriter;

    public MainController(MessageService messageService, UserDetailsRepository userDetailsRepo,
            ObjectMapper mapper) {

        this.messageService = messageService;
        this.userDetailsRepo = userDetailsRepo;

        var objectMapper = mapper.setConfig(mapper.getSerializationConfig());

        this.messageWriter = objectMapper.writerWithView(Views.FullMessage.class);
        this.profileWriter = objectMapper.writerWithView(Views.FullProfile.class);
    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user)
            throws JsonProcessingException {

        HashMap<Object, Object> data = new HashMap<>();

        if (user != null) {
            var userFromDb = userDetailsRepo.findById(user.getId()).get();
            var serializedProfile = profileWriter.writeValueAsString(userFromDb);
            model.addAttribute("profile", serializedProfile);

            var sort = Sort.by(Sort.Direction.DESC, "id");
            var pageRequest = PageRequest.of(0, MessageController.MESSAGES_PER_PAGE, sort);
            var messagePageDto = messageService.findForUser(pageRequest, user);

            var allMessagePage = messageService.findAll(pageRequest);

            var messages = messageWriter.writeValueAsString(messagePageDto.getMessages());
            var allMessages = messageWriter.writeValueAsString(allMessagePage.getMessages());

            model.addAttribute("messages", messages);
            model.addAttribute("allMessages", allMessages);
            data.put("currentPage", messagePageDto.getCurrentPage());
            data.put("totalPages", messagePageDto.getTotalPages());

            data.put("currentAllPage", allMessagePage.getCurrentPage());
            data.put("totalAllPage", allMessagePage.getTotalPages());
        } else {
            model.addAttribute("messages", "[]");
            model.addAttribute("allMessages", "[]");
            model.addAttribute("profile", "null");
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }

}
