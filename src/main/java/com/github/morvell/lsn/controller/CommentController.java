package com.github.morvell.lsn.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.morvell.lsn.domain.Comment;
import com.github.morvell.lsn.domain.User;
import com.github.morvell.lsn.service.CommentService;

/**
 * @author Андрей Захаров
 * @created 26/04/2020
 */
@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {

        this.commentService = commentService;
    }

    @PostMapping
    public Comment create(@RequestBody Comment comment, @AuthenticationPrincipal User user) {

        return commentService.create(comment, user);
    }
}
